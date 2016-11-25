package cz.muni.pa165.bookingmanager.application.service;

import cz.muni.pa165.bookingmanager.application.service.ReservationServiceImpl;
import cz.muni.pa165.bookingmanager.application.service.UserServiceImpl;
import cz.muni.pa165.bookingmanager.application.service.iface.UserService;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.persistence.dao.UserDao;
import cz.muni.pa165.bookingmanager.persistence.entity.UserEntity;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;

import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.inject.Inject;

/**
 * Tests for implementation of UserService
 * @author Mojmír Odehnal, 374422
 */
@ContextConfiguration(locations = { "/application-context.xml" } )
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {
    @Inject
    private ApplicationContext ctx;

    private UserService userService;

    @Mock
    private UserDao userDaoMock;

    private PageInfo pageInfo = new PageInfo(0, 20);

    private byte[] dummyhash1 = new byte[]{0x32, (byte) 0xFC,0x5A};
    private byte[] dummyhash2 = new byte[]{0x65,0x7A, (byte) 0xD3};
    private byte[] dummysalt1 = new byte[]{0x3A, (byte) 0xB8, (byte) 0xE1};
    private byte[] dummysalt2 = new byte[]{0x21, (byte) 0x8D,0x7F};


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(userDaoMock, ctx.getBean(Mapper.class));
    }

    @Test
    public void findAllTest() {
        UserEntity u1 = new UserEntity();
        u1.setName("Customer number 1");
        u1.setAddress("Address 1");
        u1.setEmail("mail1@mail.mail");
        u1.setPhoneNumber("123456");
        u1.setAdmin(false);
        u1.setPasswordHash(dummyhash1);
        u1.setPasswordSalt(dummysalt1);
        u1.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        UserEntity u2 = new UserEntity();
        u2.setName("Customer number 2");
        u2.setAddress("Address 2");
        u2.setEmail("mail2@mail.mail");
        u2.setAdmin(false);
        u2.setPasswordHash(dummyhash2);
        u2.setPasswordSalt(dummysalt2);
        u2.setPhoneNumber("654321");
        u2.setBirthDate(new Date(Date.valueOf("1985-08-20").getTime()));

        List<UserEntity> testUsers = new ArrayList<>();
        testUsers.add(u1);
        testUsers.add(u2);
        PageResult<UserEntity> testPage = new PageResult<>();
        testPage.setEntries(testUsers);
        testPage.setPageCount(1);
        testPage.setPageNumber(0);
        testPage.setTotalEntries(2);

        PageRequest pagerq = new PageRequest(pageInfo.getPageNumber(),pageInfo.getPageSize());
        PageImpl<UserEntity> springDataPage = new PageImpl<>(testUsers, pagerq, 2);
        when(userDaoMock.findAll(pagerq)).thenReturn(springDataPage);

        PageResult<UserEntity> resultPage = userService.findAll(pageInfo);

        assertNotNull(resultPage);
        assertEquals(testPage.getEntries(), resultPage.getEntries());
        assertEquals(testPage.getPageCount(), resultPage.getPageCount());
        assertEquals(testPage.getPageNumber(), resultPage.getPageNumber());
        assertEquals(testPage.getTotalEntries(), resultPage.getTotalEntries());
    }

    @Test
    public void findByEmailTest() {
        UserEntity u1 = new UserEntity();
        u1.setName("Customer number 1");
        u1.setAddress("Address 1");
        u1.setEmail("mail1@mail.mail");
        u1.setPhoneNumber("123456");
        u1.setAdmin(false);
        u1.setPasswordHash(dummyhash1);
        u1.setPasswordSalt(dummysalt1);
        u1.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        when(userDaoMock.findByEmail("mail1@mail.mail")).thenReturn(u1);

        Optional<UserEntity> cResult = userService.findByEmail("mail1@mail.mail");

        assertEquals(Optional.of(u1), cResult);
    }

    @Test
    public void registerUserTest() {
        UserEntity u1 = new UserEntity();
        u1.setName("Customer number 1");
        u1.setAddress("Address 1");
        u1.setEmail("mail1@mail.mail");
        u1.setPhoneNumber("123456");
        u1.setAdmin(false);
        u1.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        //UserDao userDaoMock = mock(UserDao.class);
        when(userDaoMock.save(u1)).then(new Answer<UserEntity>() {
            public UserEntity answer(InvocationOnMock invocation) throws Throwable {
                UserEntity u = (UserEntity) invocation.getArguments()[0];
                u.setId(1L);
                return u;
            }
        });

        //ReflectionTestUtils.setField(userService, "userDao", userDaoMock);
        assertTrue(userService.registerUser(u1, "password"));

        assertNotNull(u1.getId());
        assertNotNull(u1.getPasswordHash());
        assertNotNull(u1.getPasswordSalt());
        assertNotEquals("password", u1.getPasswordHash());

    }

    @Test
    public void authenticateTest() throws InvalidKeySpecException, NoSuchAlgorithmException {
        Pair<byte[],byte[]> hashAndSalt = makeHashAndSalt("password");
        byte[] hash = hashAndSalt.getLeft();
        byte[] salt = hashAndSalt.getRight();

        UserEntity u1 = new UserEntity();
        u1.setName("Customer number 1");
        u1.setAddress("Address 1");
        u1.setEmail("mail1@mail.mail");
        u1.setPhoneNumber("123456");
        u1.setAdmin(false);
        u1.setPasswordHash(hash);
        u1.setPasswordSalt(salt);
        u1.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        assertTrue(userService.authenticate(u1, "password"));
        assertFalse(userService.authenticate(u1, "not password"));
    }

    @Test
    public void updateUserTest() {
        UserEntity uOrig = new UserEntity();
        uOrig.setId(52L);
        uOrig.setName("Customer number 1");
        uOrig.setAddress("Address 1");
        uOrig.setEmail("mail1@mail.mail");
        uOrig.setPhoneNumber("123456");
        uOrig.setAdmin(false);
        uOrig.setPasswordHash(dummyhash1);
        uOrig.setPasswordSalt(dummysalt1);
        uOrig.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        UserEntity u1 = new UserEntity();
        u1.setId(52L);
        u1.setName("Customer number 2");
        u1.setAddress("Address 2");
        u1.setEmail("mail1@mail.mail");
        u1.setPhoneNumber("123456");
        u1.setAdmin(false);
        u1.setPasswordHash(dummyhash1);
        u1.setPasswordSalt(dummysalt1);
        u1.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        userService.updateUser(u1);

        assertEquals(uOrig, u1);
    }

    @Test(expected = NullPointerException.class)
    public void updateUserWithoutIdTest() {
        UserEntity u1 = new UserEntity();
        u1.setName("Customer number 1");
        u1.setAddress("Address 1");
        u1.setEmail("mail1@mail.mail");
        u1.setPhoneNumber("123456");
        u1.setAdmin(false);
        u1.setPasswordHash(dummyhash1);
        u1.setPasswordSalt(dummysalt1);
        u1.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        when(userDaoMock.save(u1)).thenReturn(u1);

        userService.updateUser(u1);
    }


    @Test
    public void isAdminTest() {
        UserEntity user = new UserEntity();
        user.setName("Customer number 1");
        user.setAddress("Address 1");
        user.setEmail("mail1@mail.mail");
        user.setPhoneNumber("123456");
        user.setAdmin(false);
        user.setPasswordHash(dummyhash1);
        user.setPasswordSalt(dummysalt1);
        user.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        UserEntity admin = new UserEntity();
        admin.setName("Customer number 1");
        admin.setAddress("Address 1");
        admin.setEmail("mail1@mail.mail");
        admin.setPhoneNumber("123456");
        admin.setAdmin(true);
        admin.setPasswordHash(dummyhash1);
        admin.setPasswordSalt(dummysalt1);
        admin.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        when(userDaoMock.findByEmail("mail1@mail.mail")).thenReturn(user).thenReturn(admin);

        assertFalse(userService.isAdmin(user));
        assertTrue(userService.isAdmin(admin));
    }


    private short HASH_AND_SALT_SIZE = 64;
    private int PBKDF2_ITER = 131072;
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
