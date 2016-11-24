package cz.muni.pa165.bookingmanager.application.facade;

import cz.muni.pa165.bookingmanager.iface.dto.UserDto;
import cz.muni.pa165.bookingmanager.iface.dto.UserLoginDto;
import cz.muni.pa165.bookingmanager.iface.facade.UserFacade;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.persistence.dao.UserDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Tests for implementation of ReservationFacade
 * @author Mojmír Odehnal, 374422
 */
@ContextConfiguration(locations = { "/application-context.xml" } )
@RunWith(SpringJUnit4ClassRunner.class)
public class UserFacadeTest {

    @Inject
    private UserFacade userFacade;

    @Inject
    private UserDao userDao;



    @Before
    public void clearDb() {
        userDao.deleteAll();
        assertEquals(0, userDao.count());
    }



    @Test
    public void findAllTest() {
        UserDto u1 = new UserDto();
        u1.setName("Customer number 1");
        u1.setAddress("Address 1");
        u1.setEmail("mail1@mail.mail");
        u1.setPhoneNumber("123456");
        u1.setAdmin(false);
        u1.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        UserDto u2 = new UserDto();
        u2.setName("Customer number 2");
        u2.setAddress("Address 2");
        u2.setEmail("mail2@mail.mail");
        u2.setAdmin(false);
        u2.setPhoneNumber("654321");
        u2.setBirthDate(new Date(Date.valueOf("1985-08-20").getTime()));

        assertTrue(userFacade.registerUser(u1, "password"));
        assertTrue(userFacade.registerUser(u2, "password"));

        List<UserDto> expectedUsers = new ArrayList<>();
        expectedUsers.add(u1);
        expectedUsers.add(u2);

        PageResult<UserDto> expectedPage = new PageResult<>();
        expectedPage.setEntries(expectedUsers);
        expectedPage.setPageCount(1);
        expectedPage.setPageNumber(1);
        expectedPage.setTotalEntries(2);

        PageInfo pageInfo = new PageInfo(0, 20);
        PageResult<UserDto> resultPage = userFacade.findAll(pageInfo);

        assertNotNull(resultPage);
        assertEquals(expectedPage.getEntries(), resultPage.getEntries());
        assertEquals(expectedPage.getPageCount(), resultPage.getPageCount());
        assertEquals(expectedPage.getPageNumber(), resultPage.getPageNumber());
        assertEquals(expectedPage.getTotalEntries(), resultPage.getTotalEntries());
    }

    @Test
    public void registerUserTest() {
        UserDto u1 = new UserDto();
        u1.setName("Customer number 1");
        u1.setAddress("Address 1");
        u1.setEmail("mail1@mail.mail");
        u1.setPhoneNumber("123456");
        u1.setAdmin(false);
        u1.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        assertTrue(userFacade.registerUser(u1, "password"));

        assertNotNull(u1.getId());
        assertNotNull(u1.getPasswordHash());
        assertNotNull(u1.getPasswordSalt());
        assertNotEquals("password", u1.getPasswordHash());
    }
    @Test
    public void findByEmailTest() {
        UserDto u1 = new UserDto();
        u1.setName("Customer number 1");
        u1.setAddress("Address 1");
        u1.setEmail("mail1@mail.mail");
        u1.setPhoneNumber("123456");
        u1.setAdmin(false);
        u1.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        assertTrue(userFacade.registerUser(u1, "password"));

        Optional<UserDto> cResult = userFacade.findByEmail("mail1@mail.mail");

        assertEquals(Optional.of(u1), cResult);
    }

    @Test
    public void authenticateTest() throws InvalidKeySpecException, NoSuchAlgorithmException {
        UserDto u1 = new UserDto();
        u1.setName("Customer number 1");
        u1.setAddress("Address 1");
        u1.setEmail("mail1@mail.mail");
        u1.setPhoneNumber("123456");
        u1.setAdmin(false);
        u1.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        assertTrue(userFacade.registerUser(u1, "password"));

        UserLoginDto login = new UserLoginDto();
        login.setEmail("mail1@mail.mail");
        login.setPasswd("password");

        assertTrue(userFacade.authenticate(login));

        login.setPasswd("not password");
        assertFalse(userFacade.authenticate(login));
    }

    @Test
    public void updateUserTest() {
        UserDto u1 = new UserDto();
        u1.setName("Customer number 1");
        u1.setAddress("Address 1");
        u1.setEmail("mail1@mail.mail");
        u1.setPhoneNumber("123456");
        u1.setAdmin(false);
        u1.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        assertTrue(userFacade.registerUser(u1, "password"));

        u1.setName("Different name");

        userFacade.updateUser(u1);

        Optional<UserDto> optionalUserChanged = userFacade.findByEmail("mail1@mail.mail");
        Optional<String> optionalNameChanged = optionalUserChanged.map(UserDto::getName);

        assertEquals(Optional.of("Different name"), optionalNameChanged);
    }

    @Test(expected = NullPointerException.class)
    public void updateUserWithoutIdTest() {
        UserDto u1 = new UserDto();
        u1.setName("Customer number 1");
        u1.setAddress("Address 1");
        u1.setEmail("mail1@mail.mail");
        u1.setPhoneNumber("123456");
        u1.setAdmin(false);
        u1.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        userFacade.updateUser(u1);
    }


    @Test
    public void isAdminTest() {
        UserDto user = new UserDto();
        user.setName("Customer");
        user.setAddress("Address 1");
        user.setEmail("mail1@mail.mail");
        user.setPhoneNumber("123456");
        user.setAdmin(false);
        user.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        UserDto admin = new UserDto();
        admin.setName("Admin");
        admin.setAddress("Address 2");
        admin.setEmail("admin@mail.mail");
        admin.setPhoneNumber("654321");
        admin.setAdmin(true);
        admin.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        assertTrue(userFacade.registerUser(user, "password"));
        assertTrue(userFacade.registerUser(admin, "password"));

        assertFalse(userFacade.isAdmin(user));
        assertTrue(userFacade.isAdmin(admin));
    }
}
