package cz.muni.pa165.bookingmanager.persistence.dao;

import cz.muni.pa165.bookingmanager.persistence.entity.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Test class for reservation DAO
 * @author Matej Harcar, 422714
 */
@ContextConfiguration("classpath:/persistence-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ReservationDaoTest {

    private RoomEntity r = new RoomEntity();
    private UserEntity c = new UserEntity();
    private HotelEntity h  = new HotelEntity();
    private int counter=0;
    long day = 3600*24*1000;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ReservationDaoTest.class);

    @Inject
    private ReservationDao reservationDao;
    @Inject
    private RoomDao roomDao;
    @Inject
    private UserDao userDao;
    @Inject
    private HotelDao hotelDao;


    @Before
    public void init(){
        // clear everything
        reservationDao.deleteAll();
        roomDao.deleteAll();
        userDao.deleteAll();
        hotelDao.deleteAll();
        assertEquals(0, reservationDao.count());
        assertEquals(0, roomDao.count());
        assertEquals(0, userDao.count());
        assertEquals(0, hotelDao.count());

        // init example hotel
        h.setName("ZX");
        h.setEmail("h@h.h");
        h.setPhoneNumber("+65535");
        h.setCity("Berlin");
        h.setStreet("Wilhelm II-strasse");
        h.setStreetNumber("32");
        Set<RoomEntity> roomset = new HashSet<>();
        h.setRooms(roomset);
        h = hotelDao.save(h);

        // init example room
        r.setBedCount(2);
        r.setName("A1");
        r.setPrice(new BigDecimal("11.53"));
        r.setDescription("Example room");
        r.setHotelId(h.getId());
        r = roomDao.save(r);

        // init example customer
        c.setEmail("c@m.d");
        c.setName("Qwer Tyui");
        c.setAddress("Asdf 53");
        c.setBirthDate(new Date(0L));
        c.setAccountState(DatabaseAccountState.CUSTOMER);
        c.setPasswordHash(new byte[]{0x3A, (byte) 0xF9, (byte) 0xA1});
        c.setPasswordSalt(new byte[]{0x5C, (byte) 0x91, (byte) 0x84});
        c.setPhoneNumber("+2147483647");
        c = userDao.save(c);
        // add room to hotel
        roomset.add(r);
        assertEquals(1,hotelDao.count());
        h = hotelDao.findOne(h.getId());
        h.setRooms(roomset);
        h = hotelDao.save(h);
    }

    @After
    public void clear(){
        reservationDao.deleteAll();
        userDao.deleteAll();
        roomDao.deleteAll();
        hotelDao.deleteAll();
        counter = 0;
    }

    @Test
    public void createReservationTest(){
        log.debug("Testing r9n creation");
        ReservationEntity res = makeReservation();
        assertNull(res.getId());

        ReservationEntity savedRes = reservationDao.save(res);
        assertNotNull(savedRes.getId());

        assertEquals(1,reservationDao.count());

        log.debug("R9n creation OK");
    }

    @Test(expected = DataAccessException.class)
    public void createNullDateTest(){
        log.debug("Testing r9n creation with one or both dates null");
        ReservationEntity reservation = makeReservation();
        reservation.setStartDate(null);
        reservation.setEndDate(null);

        reservationDao.save(reservation);
    }

    @Test(expected = DataAccessException.class)
    public void createNullRoomTest(){
        log.debug("Creating r9n w/ null room test");
        ReservationEntity r1 = makeReservation();
        r1.setRoom(null);

        reservationDao.save(r1);
    }

    @Test(expected = DataAccessException.class)
    public void createNullCustomerTest(){
        log.debug("Creating r9n w/ null customer test");
        ReservationEntity r1 = makeReservation();
        r1.setCustomer(null);
        reservationDao.save(r1);
    }

    @Test
    public void deleteReservationTest(){
        log.debug("Testing r9n deletion by entity handler");
        ReservationEntity res = makeReservation();
        ReservationEntity sres = reservationDao.save(res);
        assertEquals(1, reservationDao.count());

        reservationDao.delete(sres);
        assertEquals(0,reservationDao.count());
        log.debug("Deleting r9n by entity handler OK");
    }

    @Test
    public void deleteReservationByIdTest(){
        log.debug("Testing r9n deletion by ID");
        ReservationEntity res = makeReservation();
        ReservationEntity sres = reservationDao.save(res);
        assertEquals(1, reservationDao.count());

        reservationDao.delete(sres.getId());
        assertEquals(0,reservationDao.count());
        log.debug("Deleting r9n by ID OK");
    }

    @Test
    public void deleteAllReservationsTest(){
        log.debug("Testing all r9ns deletion");
        reservationDao.save(makeReservation());
        reservationDao.save(makeReservation());
        reservationDao.save(makeReservation());
        reservationDao.save(makeReservation());
        assertEquals(4,reservationDao.count());

        reservationDao.deleteAll();
        assertEquals(0,reservationDao.count());
        log.debug("Deleting all r9ns OK");
    }

    @Test
    public void findAllReservationsTest(){
        log.debug("Testing all r9ns find");
        reservationDao.save(makeReservation());
        reservationDao.save(makeReservation());
        reservationDao.save(makeReservation());
        reservationDao.save(makeReservation());
        assertEquals(4,reservationDao.count());

        List<ReservationEntity> allr9ns = reservationDao.findAll();
        assertEquals(4,allr9ns.size());
        log.debug("Finding all r9ns OK");
    }

    @Test
    public void findReservationByIdTest(){
        log.debug("Finding r9n by ID test");
        ReservationEntity r1 = makeReservation();
        reservationDao.save(makeReservation());
        r1 = reservationDao.save(r1);
        reservationDao.save(makeReservation());
        assertEquals(3,reservationDao.count());

        ReservationEntity found = reservationDao.findOne(r1.getId());
        assertEquals(r1.getId(),found.getId());
        log.debug("Finding r9n by ID OK");
    }

    @Test
    public void updateReservationTest(){
        log.debug("Updating r9n test");
        ReservationEntity r1 = makeReservation();
        r1 = reservationDao.save(r1);
        assertEquals(1,reservationDao.count());

        ReservationEntity updated = new ReservationEntity();
        updated.setId(r1.getId());
        updated.setRoom(r1.getRoom());
        updated.setStartDate(r1.getStartDate());
        updated.setCustomer(r1.getCustomer());
        updated.setEndDate(new Date(r1.getEndDate().getTime()+(2*day)));
        updated.setState("PAID");

        reservationDao.save(updated);
        assertEquals(1,reservationDao.count());

        ReservationEntity found = reservationDao.findOne(updated.getId());
        assertEquals(updated.getEndDate(),found.getEndDate());
        log.debug("Updating r9n OK");
    }

    @Test
    public void findByCustomerTest(){
        log.debug("Testing finding r9ns by customer");
        reservationDao.save(makeReservation());
        UserEntity c2 = new UserEntity();
        c2.setEmail("c2@m.d");
        c2.setPhoneNumber("+262143");
        c2.setAddress("Opium 23");
        c2.setBirthDate(new Date(183230805000L));
        c2.setName("OpieOP");
        c2.setAccountState(DatabaseAccountState.CUSTOMER);
        c2.setPasswordHash(new byte[]{0x3D, (byte) 0xF3, (byte) 0xA8});
        c2.setPasswordSalt(new byte[]{0x5E, (byte) 0x99, (byte) 0x87});
        userDao.save(c2);
        ReservationEntity r1 = makeReservation();
        r1.setCustomer(c2);
        reservationDao.save(r1);
        reservationDao.save(makeReservation());
        assertEquals(3,reservationDao.count());

        List<ReservationEntity> foundList = reservationDao.findByCustomer(c2);
        assertEquals(1,foundList.size());
        assertEquals(r1.getCustomer(),foundList.get(0).getCustomer());
        log.debug("Finding r9ns by customer OK");
    }

    @Test
    public void findByCustomerNoneTest(){
        log.debug("Testing finding r9ns by customer who has none");
        UserEntity c2 = new UserEntity();
        c2.setEmail("c2@m.d");
        c2.setPhoneNumber("+262143");
        c2.setAddress("Opium 23");
        c2.setBirthDate(new Date(183230805000L));
        c2.setName("OpieOP");
        c2.setAccountState(DatabaseAccountState.CUSTOMER);
        c2.setPasswordHash(new byte[]{0x5B, (byte) 0xC4, (byte) 0xD5});
        c2.setPasswordSalt(new byte[]{0x7A, (byte) 0xE3, (byte) 0x96});
        userDao.save(c2);
        reservationDao.save(makeReservation());
        reservationDao.save(makeReservation());
        reservationDao.save(makeReservation());
        assertEquals(3,reservationDao.count());

        List<ReservationEntity> foundList = reservationDao.findByCustomer(c2);
        assertEquals(0,foundList.size());
        log.debug("Finding r9ns by customer who has none OK");
    }

    @Test
    public void findNoneTest(){
        log.debug("Finding r9n with none made test");
        assertEquals(0,reservationDao.count());
        ReservationEntity r = reservationDao.findOne(1L);
        assertNull(r);
        log.debug("Finding r9n with none made OK");
    }

    @Test
    public void findByRoomTest(){
        log.debug("Finding r9n by room test");
        RoomEntity r2 = new RoomEntity();
        r2.setBedCount(3);
        r2.setDescription("r2 desc");
        r2.setName("A2");
        r2.setHotelId(h.getId());
        r2.setPrice(new BigDecimal("10"));
        r2 = roomDao.save(r2);
        Set<RoomEntity> roomset = h.getRooms();
        roomset.add(r2);
        h.setRooms(roomset);
        h = hotelDao.save(h);
        ReservationEntity re1 = makeReservation();
        re1.setRoom(r2);

        reservationDao.save(makeReservation());
        re1 = reservationDao.save(re1);
        reservationDao.save(makeReservation());
        assertEquals(3,reservationDao.count());

        List<ReservationEntity> result = reservationDao.findByRoom(r2);
        assertEquals(1,result.size());
        assertEquals(re1,result.get(0));
        log.debug("Finding r9n by room OK");
    }

    @Test
    public void findByRoomNoneTest(){
        log.debug("Finding r9n by room with none test");
        RoomEntity r2 = new RoomEntity();
        r2.setBedCount(3);
        r2.setDescription("r2 desc");
        r2.setName("A2");
        r2.setHotelId(h.getId());
        r2.setPrice(new BigDecimal("10"));
        r2 = roomDao.save(r2);

        reservationDao.save(makeReservation());
        reservationDao.save(makeReservation());
        reservationDao.save(makeReservation());
        assertEquals(3,reservationDao.count());
        List<ReservationEntity> result = reservationDao.findByRoom(r2);
        assertEquals(0,result.size());
        log.debug("Finding r9n by room w/ none OK");
    }

    @Test
    public void findByStartDateBeforeAndEndDateAfterTest(){
        ReservationEntity r1 = makeReservation();
        r1.setStartDate(Date.valueOf("2016-05-05"));
        r1.setEndDate(Date.valueOf("2016-05-10"));
        r1=reservationDao.save(r1);

        List<ReservationEntity> reslist = reservationDao.findByStartDateBeforeAndEndDateAfter(Date.valueOf("2016-05-07"),Date.valueOf("2016-05-07"));
        assertEquals(r1, reslist.get(0));

        reslist = reservationDao.findByStartDateBeforeAndEndDateAfter(Date.valueOf("2016-05-01"),Date.valueOf("2016-05-01"));
        assertEquals(0, reslist.size());
    }

    // Helper method

    private ReservationEntity makeReservation(){
        ReservationEntity re = new ReservationEntity();
        re.setCustomer(c);
        re.setRoom(r);
        re.setStartDate(new Date(Date.valueOf("2009-02-13").getTime()+day*3*counter));
        re.setEndDate(new Date(re.getStartDate().getTime()+(day*2)));
        re.setState("NEW");
        counter++;
        return re;
    }
}
