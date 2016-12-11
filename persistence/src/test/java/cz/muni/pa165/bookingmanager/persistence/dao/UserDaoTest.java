package cz.muni.pa165.bookingmanager.persistence.dao;

import cz.muni.pa165.bookingmanager.persistence.entity.DatabaseAccountState;
import cz.muni.pa165.bookingmanager.persistence.entity.UserEntity;
import java.sql.Date;
import java.util.List;

import org.springframework.test.context.ContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import javax.inject.Inject;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.springframework.dao.DataAccessException;


/**
 * Test cases for implementation of UserDao
 * @author Mojmír Odehnal, 374422
 */
@ContextConfiguration(locations = {"/persistence-context.xml"} )
@RunWith(SpringJUnit4ClassRunner.class)
public class UserDaoTest {

    @Inject
    private UserDao userDao;

    private byte[] dummyhash1 = new byte[]{0x32, (byte) 0xFC,0x5A};
    private byte[] dummyhash2 = new byte[]{0x65,0x7A, (byte) 0xD3};
    private byte[] dummysalt1 = new byte[]{0x3A, (byte) 0xB8, (byte) 0xE1};
    private byte[] dummysalt2 = new byte[]{0x21, (byte) 0x8D,0x7F};
    
    @Before
    public void clearDb(){
        userDao.deleteAll();
        assertEquals(0, userDao.count());
    }
    
    @Test
    public void findAll() {
        UserEntity c1 = new UserEntity();
        c1.setName("Customer number 1");
        c1.setAddress("Address 1");
        c1.setEmail("mail1@mail.mail");
        c1.setPhoneNumber("123456");
        c1.setAccountState(DatabaseAccountState.CUSTOMER);
        c1.setPasswordHash(dummyhash1);
        c1.setPasswordSalt(dummysalt1);
        c1.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));
        userDao.save(c1);
        
        UserEntity c2 = new UserEntity();
        c2.setName("Customer number 2");
        c2.setAddress("Address 2");
        c2.setEmail("mail2@mail.mail");
        c2.setAccountState(DatabaseAccountState.CUSTOMER);
        c2.setPasswordHash(dummyhash2);
        c2.setPasswordSalt(dummysalt2);
        c2.setPhoneNumber("654321");
        c2.setBirthDate(new Date(Date.valueOf("1985-08-20").getTime()));
        userDao.save(c2);

        List<UserEntity> storedCustomers  = userDao.findAll();

        assertEquals(2, storedCustomers.size());
        
        UserEntity c1check = new UserEntity();
        c1check.setName("Customer number 1");
        c1check.setAddress("Address 1");
        c1check.setEmail("mail1@mail.mail");
        c1check.setPhoneNumber("123456");
        c1.setPasswordHash(dummyhash1);
        c1.setPasswordSalt(dummysalt1);
        c1check.setAccountState(DatabaseAccountState.CUSTOMER);
        c1check.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));
        
        UserEntity c2check = new UserEntity();
        c2check.setName("Customer number 2");
        c2check.setAddress("Address 2");
        c2check.setEmail("mail2@mail.mail");
        c2check.setPhoneNumber("654321");
        c2.setPasswordHash(dummyhash2);
        c2.setPasswordSalt(dummysalt2);
        c2check.setAccountState(DatabaseAccountState.CUSTOMER);
        c2check.setBirthDate(new Date(Date.valueOf("1985-08-20").getTime()));

        assertTrue(storedCustomers.contains(c1check));
        assertTrue(storedCustomers.contains(c2check));
        
        userDao.delete(c1);
        userDao.delete(c2);
    }
    
    @Test(expected=DataAccessException.class)
    public void saveCustomerWithNullAsName() {
        UserEntity c = new UserEntity();
        c.setName(null);
        c.setAddress("Address");
        c.setEmail("mail@mail.mail");
        c.setPhoneNumber("123456");
        c.setPasswordHash(dummyhash2);
        c.setPasswordSalt(dummysalt2);
        c.setAccountState(DatabaseAccountState.CUSTOMER);
        c.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));
        userDao.save(c);
    }
    
    @Test(expected=DataAccessException.class)
    public void saveCustomerWithNullAsAddress() {
        UserEntity c = new UserEntity();
        c.setName("Customer Name");
        c.setAddress(null);
        c.setEmail("mail@mail.mail");
        c.setPhoneNumber("123456");
        c.setPasswordHash(dummyhash2);
        c.setPasswordSalt(dummysalt2);
        c.setAccountState(DatabaseAccountState.CUSTOMER);
        c.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));
        userDao.save(c);
    }
    // I checked two. I expect others to not null properties will behave the same.
    // No need to copy-paste too much I guess
    
    @Test(expected=DataAccessException.class)
    public void saveDuplicateEmail() {
        UserEntity c1 = new UserEntity();
        c1.setName("Customer number 1");
        c1.setAddress("Address 1");
        c1.setEmail("mail1@mail.mail");
        c1.setPhoneNumber("123456");
        c1.setPasswordHash(dummyhash1);
        c1.setPasswordSalt(dummysalt1);
        c1.setAccountState(DatabaseAccountState.CUSTOMER);
        c1.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));
        userDao.save(c1);
        
        UserEntity c2 = new UserEntity();
        c2.setName("Customer number 2");
        c2.setAddress("Address 2");
        c2.setEmail("mail1@mail.mail");
        c2.setPhoneNumber("654321");
        c2.setPasswordHash(dummyhash2);
        c2.setPasswordSalt(dummysalt2);
        c2.setAccountState(DatabaseAccountState.CUSTOMER);
        c2.setBirthDate(new Date(Date.valueOf("1985-08-20").getTime()));
        userDao.save(c2);
    }
    
    @Test(expected=DataAccessException.class)
    public void saveDuplicatePhoneNumber() {
        UserEntity c1 = new UserEntity();
        c1.setName("Customer number 1");
        c1.setAddress("Address 1");
        c1.setEmail("mail1@mail.mail");
        c1.setPasswordHash(dummyhash1);
        c1.setPasswordSalt(dummysalt1);
        c1.setPhoneNumber("123456");
        c1.setAccountState(DatabaseAccountState.CUSTOMER);
        c1.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));
        userDao.save(c1);
        
        UserEntity c2 = new UserEntity();
        c2.setName("Customer number 2");
        c2.setAddress("Address 2");
        c2.setEmail("mail2@mail.mail");
        c2.setPasswordHash(dummyhash2);
        c2.setPasswordSalt(dummysalt2);
        c2.setPhoneNumber("123456");
        c2.setAccountState(DatabaseAccountState.CUSTOMER);
        c2.setBirthDate(new Date(Date.valueOf("1985-08-20").getTime()));
        userDao.save(c2);
    }

    
    @Test
    public void saveAndFindAndUpdate() {
        UserEntity original = new UserEntity();
        original.setName("Customer original");
        original.setAddress("Address");
        original.setEmail("mail@mail.mail");
        original.setPhoneNumber("123456");
        original.setPasswordHash(dummyhash2);
        original.setPasswordSalt(dummysalt2);
        original.setAccountState(DatabaseAccountState.CUSTOMER);
        original.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));
        
        assertNull(original.getId());
        userDao.save(original);
        assertNotNull(original.getId());
        
        assertEquals(1, userDao.count());
        
        UserEntity retrieved = userDao.findOne(original.getId());
        assertEquals("Customer original", retrieved.getName());
        assertEquals("Address", retrieved.getAddress());
        
        assertEquals(retrieved, userDao.findAll().get(0));
        
        retrieved.setName("Customer updated");
        userDao.save(retrieved);
        UserEntity retrieved2 = userDao.findOne(retrieved.getId());
        assertEquals("Customer updated", retrieved2.getName());
        assertEquals("Address", retrieved2.getAddress());
        
        original.setAddress("Address updated");
        userDao.save(original);
        UserEntity retrieved3 = userDao.findOne(retrieved.getId());
        assertEquals("Customer original", retrieved3.getName());
        assertEquals("Address updated", retrieved3.getAddress());
    }
    
    @Test
    public void delete() {
        UserEntity c = new UserEntity();
        c.setName("Customer");
        c.setAddress("Address");
        c.setEmail("mail@mail.mail");
        c.setPhoneNumber("123456");
        c.setPasswordHash(dummyhash2);
        c.setPasswordSalt(dummysalt2);
        c.setAccountState(DatabaseAccountState.CUSTOMER);
        c.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));
        
        userDao.save(c);
        assertNotNull(userDao.findOne(c.getId()));
        
        userDao.delete(c);
        assertNull(userDao.findOne(c.getId()));
    }

    @Test
    public void findByPhoneNumberTest(){
        UserEntity c = new UserEntity();
        c.setName("Customer");
        c.setAddress("Address");
        c.setEmail("mail@mail.mail");
        c.setPhoneNumber("123456");
        c.setPasswordHash(dummyhash2);
        c.setPasswordSalt(dummysalt2);
        c.setAccountState(DatabaseAccountState.CUSTOMER);
        c.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        c = userDao.save(c);
        assertEquals(c,userDao.findByPhoneNumber(c.getPhoneNumber()));
    }

    @Test
    public void findByIdTest(){
        UserEntity c = new UserEntity();
        c.setName("Customer");
        c.setAddress("Address");
        c.setEmail("mail@mail.mail");
        c.setPhoneNumber("123456");
        c.setPasswordHash(dummyhash2);
        c.setPasswordSalt(dummysalt2);
        c.setAccountState(DatabaseAccountState.CUSTOMER);
        c.setBirthDate(new Date(Date.valueOf("1990-03-05").getTime()));

        c = userDao.save(c);
        assertEquals(c,userDao.findById(c.getId()));
    }

}
