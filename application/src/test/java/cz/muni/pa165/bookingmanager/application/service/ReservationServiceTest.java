package cz.muni.pa165.bookingmanager.application.service;

import cz.muni.pa165.bookingmanager.application.service.iface.ReservationService;
import cz.muni.pa165.bookingmanager.iface.dto.ReservationState;
import cz.muni.pa165.bookingmanager.iface.util.HotelStatistics;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.ReservationFilter;
import cz.muni.pa165.bookingmanager.persistence.dao.HotelDao;
import cz.muni.pa165.bookingmanager.persistence.dao.ReservationDao;
import cz.muni.pa165.bookingmanager.persistence.dao.RoomDao;
import cz.muni.pa165.bookingmanager.persistence.dao.UserDao;
import cz.muni.pa165.bookingmanager.persistence.entity.*;
import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@ContextConfiguration(locations = { "/application-context.xml" } )
@RunWith(SpringJUnit4ClassRunner.class)
public class ReservationServiceTest {

    @Inject
    private ApplicationContext ctx;

    @Mock
    private UserDao userDao;

    @Mock
    private ReservationDao reservationDao;

    @Mock
    private RoomDao roomDao;

    @Mock
    private HotelDao hotelDao;

    private UserEntity user;
    private HotelEntity hotel;
    private RoomEntity room;
    private ReservationEntity r9n;

    private ReservationService rs;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        rs = new ReservationServiceImpl(reservationDao, ctx.getBean(Mapper.class));

        user = new UserEntity();
        user.setAccountState(DatabaseAccountState.CUSTOMER);
        user.setBirthDate(Date.valueOf("1975-03-21"));
        user.setPasswordSalt(new byte[]{0x3f, 0x2a, 0x09});
        user.setAddress("Address A, City C, postcode P, country C");
        user.setEmail("address@host.domain");
        user.setPhoneNumber("9862498");
        user.setName("Asdf Qwer");
        user.setPasswordHash(new byte[]{0x2e, 0x7c, 0x44});
        user.setId(1L);

        hotel = new HotelEntity();
        hotel.setEmail("hotel@hotelnet.com");
        hotel.setId(1L);
        hotel.setName("Feynman Hotel");
        hotel.setPhoneNumber("18795427");
        hotel.setStreetNumber("53A");
        hotel.setStreet("Einstein Street");
        hotel.setCity("Meitnerville");
        Set<RoomEntity> roomset = new HashSet<>();
        hotel.setRooms(roomset);

        room = new RoomEntity();
        room.setBedCount(3);
        room.setDescription("Nondescript room");
        room.setName("Room 101");
        room.setPrice(new BigDecimal("15.36"));
        room.setId(1L);
        room.setHotelId(1L);
        roomset.add(room);
        hotel.setRooms(roomset);

        r9n = new ReservationEntity();
        r9n.setEndDate(Date.valueOf("2016-09-03"));
        r9n.setStartDate(Date.valueOf("2016-08-30"));
        r9n.setRoom(room);
        r9n.setCustomer(user);
        r9n.setState(String.valueOf(ReservationState.NEW));
    }

    @Test
    public void createReservationTest(){
        when(reservationDao.save(r9n)).thenReturn(returnR9nWithSetID(r9n));
        ReservationEntity r9nx = rs.createReservation(r9n);
        assertEquals(r9nx.getId(),new Long(1L));
        assertEquals(r9n, r9nx);
    }

    @Test
    public void updateReservationTest(){
        when(reservationDao.save(r9n)).thenReturn(returnR9nWithSetEndDate(r9n));
        r9n.setEndDate(Date.valueOf("2016-09-02"));
        ReservationEntity r9nx = reservationDao.save(r9n);
        assertEquals(r9n, r9nx);
    }

    @Test
    public void findByIdNoneTest(){
        Optional<ReservationEntity> x = Optional.ofNullable(null);
        when(reservationDao.findOne(2L)).thenReturn(null);
        assertEquals(x, rs.findById(2L));
    }

    @Test
    public void findByIdExistsTest(){
        Optional<ReservationEntity> x = Optional.of(r9n);
        when(reservationDao.findOne(1L)).thenReturn(r9n);
        assertEquals(x, rs.findById(1L));
    }

    @Test
    public void findAllTest(){
        ReservationEntity r9n2 = new ReservationEntity();
        r9n2.setCustomer(user);
        r9n2.setRoom(room);
        r9n2.setState(String.valueOf(ReservationState.ENDED));
        r9n2.setStartDate(Date.valueOf("2015-05-05"));
        r9n2.setEndDate(Date.valueOf("2015-05-10"));
        List<ReservationEntity> elist = new ArrayList<>();
        elist.add(r9n);
        elist.add(r9n2);
        PageInfo info = new PageInfo(1,10);
        org.springframework.data.domain.Page <ReservationEntity> x
                = new org.springframework.data.domain.PageImpl<ReservationEntity>
                (elist,new PageRequest(info.getPageNumber(),info.getPageSize()),2);
        when(reservationDao.findAll(new PageRequest(info.getPageNumber(),info.getPageSize()))).thenReturn(x);
        List<ReservationEntity> elist2 = reservationDao.findAll(new PageRequest(info.getPageNumber(),info.getPageSize())).getContent();
        assertEquals(elist,elist2);
    }

    @Test
    public void findFilteredTest(){
        ReservationFilter f = new ReservationFilter();
        f.setEndsAfter(Date.valueOf("2016-09-01"));
        f.setStartsBefore(Date.valueOf("2016-09-01"));
        f.setCustomerId(user.getId());
        f.setRoomId(room.getId());
        f.setState(ReservationState.NEW);
        Pageable prq = new PageRequest(1,10);
        PageInfo info = new PageInfo(1,10);
        List<ReservationEntity> elist = new ArrayList<>();
        elist.add(r9n);
        PageImpl p = new PageImpl<>(elist,new PageRequest(info.getPageNumber(),info.getPageSize()),2);
        when(reservationDao.findByOptionalCustomCriteria
                (f.getRoomId().get(),f.getCustomerId().get(),f.getStartsBefore().get(),f.getEndsAfter().get()
                        ,String.valueOf(f.getState().get()),prq)).thenReturn(p);
        PageResult<ReservationEntity> x = rs.findFiltered(f,info);
        assertNotNull(x);
        assertEquals(elist.size(),x.getEntrySize());
    }

    @Test
    public void findFilteredNoneTest(){
        ReservationFilter f = new ReservationFilter();
        f.setEndsAfter(Date.valueOf("2016-09-01"));
        f.setStartsBefore(Date.valueOf("2016-09-01"));
        f.setCustomerId(user.getId());
        f.setRoomId(room.getId());
        f.setState(ReservationState.CANCELLED);
        // one item in difference, when using an AND of required attribute values, is enough
        Pageable prq = new PageRequest(0,10);
        PageInfo info = new PageInfo(0,10);

        when(reservationDao.findByOptionalCustomCriteria(f.getRoomId().get(),
                f.getCustomerId().get(),
                f.getStartsBefore().get(),
                f.getEndsAfter().get(),
                f.getState().get().toString(),
                prq)).
                thenReturn(new PageImpl<>(new LinkedList<>(), prq, 1L));

        PageResult<ReservationEntity> y = rs.findFiltered(f,info);
        assertNotNull(y);
        assertEquals(0, y.getEntrySize());
    }



    @Test
    public void gatherHotelStatisticsTest() {
        RoomEntity room2 = new RoomEntity();
        room2.setBedCount(3);
        room2.setDescription("Nondescript room");
        room2.setName("Room 101");
        room2.setPrice(new BigDecimal("15.36"));
        room2.setId(2L);
        room2.setHotelId(1L);
        hotel.getRooms().add(room2);

        ReservationEntity r9n1 = new ReservationEntity();
        r9n1.setStartDate(Date.valueOf("2016-08-30"));
        r9n1.setEndDate(Date.valueOf("2016-09-03"));
        r9n1.setRoom(room);
        r9n1.setCustomer(user);
        r9n1.setState(String.valueOf(ReservationState.PAID));

        ReservationEntity r9n2 = new ReservationEntity();
        r9n2.setStartDate(Date.valueOf("2016-09-02"));
        r9n2.setEndDate(Date.valueOf("2016-09-05"));
        r9n2.setRoom(room2);
        r9n2.setCustomer(user);
        r9n2.setState(String.valueOf(ReservationState.ENDED));

        ReservationEntity r9n3 = new ReservationEntity();
        r9n3.setStartDate(Date.valueOf("2016-09-01"));
        r9n3.setEndDate(Date.valueOf("2016-09-08"));
        r9n3.setRoom(room2);
        r9n3.setCustomer(user);
        r9n3.setState(String.valueOf(ReservationState.CANCELLED)); // cancelled should not be counted into statistics

        List<ReservationEntity> returnValueFromDao = new ArrayList<>();
        returnValueFromDao.add(r9n1);
        returnValueFromDao.add(r9n2);
        returnValueFromDao.add(r9n3);

        when(reservationDao.findByHotelIdValidInInterval(hotel.getId(), Date.valueOf("2016-09-01"),
                Date.valueOf("2016-09-05"))).thenReturn(returnValueFromDao);

        HotelStatistics result = rs.gatherHotelStatistics(hotel.getId(), Date.valueOf("2016-09-01"),
                Date.valueOf("2016-09-05"));

        System.out.println(result);

        assertEquals(2, result.getNumberOfCompletedReservations());
        assertEquals(new BigDecimal("107.52"), result.getRevenue());
        assertEquals(1.4, result.getAverageRoomUsage(), 0.01);
        assertEquals(3.5, result.getAverageReservationLength(), 0.01);
    }

    private ReservationEntity returnR9nWithSetID(ReservationEntity r9n) {
        ReservationEntity rv = new ReservationEntity();
        rv.setEndDate(r9n.getEndDate());
        rv.setRoom(r9n.getRoom());
        rv.setId(1L);
        rv.setStartDate(r9n.getStartDate());
        rv.setCustomer(r9n.getCustomer());
        rv.setState(r9n.getState());
        return rv;
    }

    private ReservationEntity returnR9nWithSetEndDate(ReservationEntity r9n) {
        ReservationEntity rv = new ReservationEntity();
        rv.setEndDate(Date.valueOf("2016-09-02"));
        rv.setRoom(r9n.getRoom());
        rv.setStartDate(r9n.getStartDate());
        rv.setState(r9n.getState());
        rv.setCustomer(r9n.getCustomer());
        return rv;
    }

}
