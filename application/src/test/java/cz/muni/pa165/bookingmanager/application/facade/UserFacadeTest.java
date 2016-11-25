package cz.muni.pa165.bookingmanager.application.facade;

import cz.muni.pa165.bookingmanager.application.service.iface.UserService;
import cz.muni.pa165.bookingmanager.iface.dto.UserDto;
import cz.muni.pa165.bookingmanager.iface.dto.UserLoginDto;
import cz.muni.pa165.bookingmanager.iface.facade.UserFacade;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.persistence.entity.UserEntity;
import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests for implementation of UserFacade
 * @author Mojmír Odehnal, 374422
 */
@ContextConfiguration(locations = { "/application-context.xml" } )
@RunWith(SpringJUnit4ClassRunner.class)
public class UserFacadeTest {

    @Inject
    private ApplicationContext ctx;

    @Inject
    private Mapper mapper;

    private UserFacade userFacade;

    @Mock
    private UserService userServiceMock;

    private byte[] dummyhash1 = new byte[]{0x32, (byte) 0xFC,0x5A};
    private byte[] dummysalt1 = new byte[]{0x3A, (byte) 0xB8, (byte) 0xE1};

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        userFacade = new UserFacadeImpl(userServiceMock, ctx.getBean(Mapper.class));
    }

    private PageResult<UserEntity> mapPageResultToEntities(PageResult<UserDto> dtoPage){
        List<UserEntity> dtos = dtoPage.getEntries()
                .stream()
                .map(x -> mapper.map(x, UserEntity.class))
                .collect(Collectors.toList());

        PageResult<UserEntity> result = new PageResult<>();
        mapper.map(dtoPage, result);
        result.setEntries(dtos);

        return result;
    }

    @Test
    public void findAllTest() {
        UserDto u1 = new UserDto();
        u1.setId(1L);
        u1.setName("Customer number 1");
        u1.setAddress("Address 1");
        u1.setEmail("mail1@mail.mail");
        u1.setPhoneNumber("123456");
        u1.setAdmin(false);
        u1.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        UserDto u2 = new UserDto();
        u2.setId(2L);
        u2.setName("Customer number 2");
        u2.setAddress("Address 2");
        u2.setEmail("mail2@mail.mail");
        u2.setAdmin(false);
        u2.setPhoneNumber("654321");
        u2.setBirthDate(new Date(Date.valueOf("1985-08-20").getTime()));

        List<UserDto> testUsersDtos = new ArrayList<>();
        testUsersDtos.add(u1);
        testUsersDtos.add(u2);

        PageResult<UserDto> expectedPage = new PageResult<>();
        expectedPage.setEntries(testUsersDtos);
        expectedPage.setPageCount(1);
        expectedPage.setPageNumber(0);
        expectedPage.setTotalEntries(2);

        PageInfo pageInfo = new PageInfo(0, 20);

        PageResult<UserEntity> pageForMock = mapPageResultToEntities(expectedPage);

        when(userServiceMock.findAll(pageInfo)).thenReturn(pageForMock);
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

        UserEntity u1entity = mapper.map(u1, UserEntity.class);

        when(userServiceMock.registerUser(u1entity, "password")).then(new Answer<Boolean>() {
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                UserEntity u = (UserEntity) invocation.getArguments()[0];
                u.setId(1L);
                u.setPasswordHash(dummyhash1);
                u.setPasswordSalt(dummysalt1);
                return true;
            }
        });

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

        UserEntity u1entity = mapper.map(u1, UserEntity.class);

        when(userServiceMock.findByEmail("mail1@mail.mail")).thenReturn(Optional.of(u1entity));

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
        u1.setPasswordHash(dummyhash1);
        u1.setPasswordSalt(dummysalt1);
        u1.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        UserEntity u1entity = mapper.map(u1, UserEntity.class);

        when(userServiceMock.findByEmail("mail1@mail.mail")).thenReturn(Optional.of(u1entity));
        when(userServiceMock.authenticate(u1entity,"password")).thenReturn(true);
        when(userServiceMock.authenticate(u1entity,"not password")).thenReturn(false);

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
        u1.setId(1L);
        u1.setName("Different name");
        u1.setAddress("Address 1");
        u1.setEmail("mail1@mail.mail");
        u1.setPhoneNumber("123456");
        u1.setAdmin(false);
        u1.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        UserEntity u1entity = mapper.map(u1, UserEntity.class);

        when(userServiceMock.updateUser(u1entity)).thenReturn(u1entity);

        UserDto updated = userFacade.updateUser(u1);

        assertEquals(u1, updated);
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

        UserEntity userEntity = mapper.map(user, UserEntity.class);
        UserEntity adminEntity = mapper.map(admin, UserEntity.class);

        when(userServiceMock.isAdmin(userEntity)).thenReturn(false);
        when(userServiceMock.isAdmin(adminEntity)).thenReturn(true);

        assertFalse(userFacade.isAdmin(user));
        assertTrue(userFacade.isAdmin(admin));
    }


}
