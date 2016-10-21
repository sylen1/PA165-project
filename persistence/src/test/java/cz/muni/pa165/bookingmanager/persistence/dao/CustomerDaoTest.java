package cz.muni.pa165.bookingmanager.persistence.dao;

import cz.muni.pa165.bookingmanager.persistence.entity.CustomerEntity;
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


@ContextConfiguration(locations = { "/application-context.xml" } )
@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerDaoTest {

    @Inject
    private CustomerDao customerDao;
    
    
    @Before
    public void clearDb(){
        customerDao.deleteAll();
        assertEquals(0, customerDao.count());
    }
    
    @Test
    public void findAll() {
        CustomerEntity c1 = new CustomerEntity();
        c1.setName("Customer number 1");
        c1.setAddress("Address 1");
        c1.setEmail("mail1@mail.mail");
        c1.setPhoneNumber("123456");
        c1.setBirthDate(new Date(636595200000L));
        customerDao.save(c1);
        
        CustomerEntity c2 = new CustomerEntity();
        c2.setName("Customer number 2");
        c2.setAddress("Address 2");
        c2.setEmail("mail2@mail.mail");
        c2.setPhoneNumber("654321");
        c2.setBirthDate(new Date(493344000000L));
        customerDao.save(c2);

        List<CustomerEntity> storedCustomers  = customerDao.findAll();

        assertEquals(2, storedCustomers.size());
        
        CustomerEntity c1check = new CustomerEntity();
        c1check.setName("Customer number 1");
        c1check.setAddress("Address 1");
        c1check.setEmail("mail1@mail.mail");
        c1check.setPhoneNumber("123456");
        c1check.setBirthDate(new Date(636595200000L));
        
        CustomerEntity c2check = new CustomerEntity();
        c2check.setName("Customer number 2");
        c2check.setAddress("Address 2");
        c2check.setEmail("mail2@mail.mail");
        c2check.setPhoneNumber("654321");
        c2check.setBirthDate(new Date(493344000000L));

        assertTrue(storedCustomers.contains(c1check));
        assertTrue(storedCustomers.contains(c2check));
        
        customerDao.delete(c1);
        customerDao.delete(c2);
    }
    
    @Test(expected=DataAccessException.class)
    public void saveCustomerWithNullAsName() {
        CustomerEntity c = new CustomerEntity();
        c.setName(null);
        c.setAddress("Address");
        c.setEmail("mail@mail.mail");
        c.setPhoneNumber("123456");
        c.setBirthDate(new Date(636595200000L));
        customerDao.save(c);
    }
    
    @Test(expected=DataAccessException.class)
    public void saveCustomerWithNullAsAddress() {
        CustomerEntity c = new CustomerEntity();
        c.setName("Customer Name");
        c.setAddress(null);
        c.setEmail("mail@mail.mail");
        c.setPhoneNumber("123456");
        c.setBirthDate(new Date(636595200000L));
        customerDao.save(c);
    }
    // I checked two. I expect others to not null properties will behave the same.
    // No need to copy-paste too much I guess
    
    @Test(expected=DataAccessException.class)
    public void saveDuplicateEmail() {
        CustomerEntity c1 = new CustomerEntity();
        c1.setName("Customer number 1");
        c1.setAddress("Address 1");
        c1.setEmail("mail1@mail.mail");
        c1.setPhoneNumber("123456");
        c1.setBirthDate(new Date(636595200000L));
        customerDao.save(c1);
        
        CustomerEntity c2 = new CustomerEntity();
        c2.setName("Customer number 2");
        c2.setAddress("Address 2");
        c2.setEmail("mail1@mail.mail");
        c2.setPhoneNumber("654321");
        c2.setBirthDate(new Date(493344000000L));
        customerDao.save(c2);
    }
    
    @Test(expected=DataAccessException.class)
    public void saveDuplicatePhoneNumber() {
        CustomerEntity c1 = new CustomerEntity();
        c1.setName("Customer number 1");
        c1.setAddress("Address 1");
        c1.setEmail("mail1@mail.mail");
        c1.setPhoneNumber("123456");
        c1.setBirthDate(new Date(636595200000L));
        customerDao.save(c1);
        
        CustomerEntity c2 = new CustomerEntity();
        c2.setName("Customer number 2");
        c2.setAddress("Address 2");
        c2.setEmail("mail2@mail.mail");
        c2.setPhoneNumber("123456");
        c2.setBirthDate(new Date(493344000000L));
        customerDao.save(c2);
    }

    
    @Test
    public void saveAndFindAndUpdate() {
        CustomerEntity original = new CustomerEntity();
        original.setName("Customer original");
        original.setAddress("Address");
        original.setEmail("mail@mail.mail");
        original.setPhoneNumber("123456");
        original.setBirthDate(new Date(636595200000L));
        
        assertNull(original.getId());
        customerDao.save(original);
        assertNotNull(original.getId());
        
        assertEquals(1, customerDao.count());
        
        CustomerEntity retrieved = customerDao.findOne(original.getId());
        assertEquals("Customer original", retrieved.getName());
        assertEquals("Address", retrieved.getAddress());
        
        assertEquals(retrieved, customerDao.findAll().get(0));
        
        retrieved.setName("Customer updated");
        customerDao.save(retrieved);
        CustomerEntity retrieved2 = customerDao.findOne(retrieved.getId());
        assertEquals("Customer updated", retrieved2.getName());
        assertEquals("Address", retrieved2.getAddress());
        
        original.setAddress("Address updated");
        customerDao.save(original);
        CustomerEntity retrieved3 = customerDao.findOne(retrieved.getId());
        assertEquals("Customer original", retrieved3.getName());
        assertEquals("Address updated", retrieved3.getAddress());
    }
    
    @Test()
    public void delete() {
        CustomerEntity c = new CustomerEntity();
        c.setName("Customer");
        c.setAddress("Address");
        c.setEmail("mail@mail.mail");
        c.setPhoneNumber("123456");
        c.setBirthDate(new Date(636595200000L));
        
        customerDao.save(c);
        assertNotNull(customerDao.findOne(c.getId()));
        
        customerDao.delete(c);
        assertNull(customerDao.findOne(c.getId()));
    }
}
