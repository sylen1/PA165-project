package cz.muni.pa165.bookingmanager.application.service;

import cz.muni.pa165.bookingmanager.application.service.iface.UserService;
import cz.muni.pa165.bookingmanager.iface.util.Page;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.persistence.dao.UserDao;
import cz.muni.pa165.bookingmanager.persistence.entity.UserEntity;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
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

    @Inject
    private UserDao userDao;

    @Override
    public Page<UserEntity> findAll(PageInfo pageInfo) {
        Pageable pagerq = new PageRequest(pageInfo.getPageNumber(),pageInfo.getPageSize());
        List<UserEntity> entities = userDao.findAll(pagerq).getContent();
        return new Page<>(entities, entities.size(), pageInfo);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return Optional.ofNullable(userDao.findByEmail(email));
    }

    @Override
    public boolean registerUser(UserEntity user, String passwd) {
        Pair<byte[],byte[]> hashSalt;
        try {
            hashSalt = makeHashAndSalt(passwd);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            LOG.warn(e);
            return false;
        }
        user.setPasswordHash(hashSalt.getKey());
        user.setPasswordSalt(hashSalt.getValue());

        userDao.save(user);
        return true;
    }

    @Override
    public boolean isAdmin(UserEntity user) {
        return userDao.findByEmail(user.getEmail()).isAdmin();
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
