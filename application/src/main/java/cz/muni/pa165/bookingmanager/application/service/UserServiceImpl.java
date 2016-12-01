package cz.muni.pa165.bookingmanager.application.service;

import cz.muni.pa165.bookingmanager.application.service.iface.UserService;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.persistence.dao.UserDao;
import cz.muni.pa165.bookingmanager.persistence.dao.UserTokenDao;
import cz.muni.pa165.bookingmanager.persistence.entity.DatabaseAccountState;
import cz.muni.pa165.bookingmanager.persistence.entity.UserEntity;
import cz.muni.pa165.bookingmanager.persistence.entity.UserToken;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Optional;

/**
 * User service interface implementation
 * @author Matej Harcar, 422714
 */
public class UserServiceImpl implements UserService{
    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);

    private short HASH_AND_SALT_SIZE = 64;
    // 64B = 512b = default block and output size for SHA512 > SHA256
    private int PBKDF2_ITER = 131072;
    // 100'000 rounds used in 2011 by LastPass;
    // we're (presumably) a smaller target but it's no longer 2011

    private UserDao userDao;
    private Mapper mapper;
    private UserTokenDao userTokenDao;

    @Inject
    public UserServiceImpl(UserDao userDao, Mapper mapper, UserTokenDao userTokenDao) {
        this.userDao = userDao;
        this.mapper = mapper;
        this.userTokenDao = userTokenDao;
    }

    @Override
    public PageResult<UserEntity> findAll(PageInfo pageInfo) {
        Pageable pagerq = new PageRequest(pageInfo.getPageNumber(),pageInfo.getPageSize());
        Page<UserEntity> findResult = userDao.findAll(pagerq);
        PageResult<UserEntity> rv = new PageResult<>();
        mapper.map(findResult,rv);
        rv.setEntries(findResult.getContent());
        return rv;
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return Optional.ofNullable(userDao.findByEmail(email));
    }

    @Override
    public Pair<UserEntity, String> registerUser(UserEntity user, String passwd) {
        Pair<byte[],byte[]> hashSalt;
        try {
            hashSalt = makeHashAndSalt(passwd);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            LOG.warn(e);
            return null;
        }
        user.setPasswordHash(hashSalt.getKey());
        user.setPasswordSalt(hashSalt.getValue());

        user = userDao.save(user);
        String a = user.getEmail();
        SecureRandom rng = new SecureRandom();
        byte[] r = new byte[32];
        rng.nextBytes(r);
        byte[] rv2;
        try {
            rv2 = pbkdf2(a.toCharArray(),r,1024,32);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return null;
        }

        String rv2String = Arrays.toString(rv2);
        userTokenDao.save(new UserToken(user.getEmail(), rv2String));
        return Pair.of(user, rv2String);
    }

    @Override
    public boolean isAdmin(UserEntity user) {
        return userDao.findByEmail(user.getEmail()).getAccountState().equals(DatabaseAccountState.ADMIN);
    }

    @Override
    public boolean authenticate(UserEntity user, String passwd) {
        Validate.notNull(user);
        Validate.notNull(passwd);
        boolean result = false;
        try {
            result = checkPassword(passwd, user.getPasswordHash(), user.getPasswordSalt());
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            LOG.warn(e);
        }
        return result;
    }

    @Override
    public UserEntity updateUser(UserEntity user) {
        Validate.notNull(user.getId());
        return userDao.save(user);
    }

    @Override
    public boolean confirmUser(String email, String token) {
        Validate.notNull(email);
        Validate.notNull(token);
        Optional<UserToken> userToken = userTokenDao.findByEmail(email);
        if (userToken.isPresent() && token.equals(userToken.get().getToken())){
            UserEntity user = userDao.findByEmail(email);
            user.setAccountState(DatabaseAccountState.CUSTOMER);
            updateUser(user);
            userTokenDao.delete(userToken.get());
            return true;
        }
        return false;
    }

    private Pair<byte[],byte[]> makeHashAndSalt(String passwd) throws InvalidKeySpecException, NoSuchAlgorithmException {
        SecureRandom prng = new SecureRandom();

        byte[] salt = new byte[HASH_AND_SALT_SIZE];
        prng.nextBytes(salt);

        byte[] hash = pbkdf2(passwd.toCharArray(), salt, PBKDF2_ITER, HASH_AND_SALT_SIZE);
        return Pair.of(hash, salt);
    }

    private byte[] pbkdf2(char[] pass, byte[] salt, int iter, int bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(pass,salt,iter,bytes*8);
        return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512").generateSecret(spec).getEncoded();
    }

    private boolean checkPassword(String passwd, byte[] expected, byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
        Validate.notNull(passwd);
        Validate.notNull(expected);
        byte[] test = pbkdf2(passwd.toCharArray(), salt, PBKDF2_ITER,HASH_AND_SALT_SIZE);
        return slowEq(test, expected);
    }

    private boolean slowEq(byte[] a, byte[] b) {
        int diff;
        diff = a.length == b.length ? 0:65536;
        for (int i = 0; i < a.length && i < b.length; i++){
            diff |= a[i] ^ b[i];
        }
        return diff==0;
    }


}
