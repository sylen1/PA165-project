package cz.muni.pa165.bookingmanager.applictaion.service;

import cz.muni.pa165.bookingmanager.application.service.ReservationServiceImpl;
import cz.muni.pa165.bookingmanager.application.service.iface.ReservationService;
import cz.muni.pa165.bookingmanager.iface.dto.ReservationState;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.ReservationFilter;
import cz.muni.pa165.bookingmanager.persistence.dao.HotelDao;
import cz.muni.pa165.bookingmanager.persistence.dao.ReservationDao;
import cz.muni.pa165.bookingmanager.persistence.dao.RoomDao;
import cz.muni.pa165.bookingmanager.persistence.dao.UserDao;
import cz.muni.pa165.bookingmanager.persistence.entity.HotelEntity;
import cz.muni.pa165.bookingmanager.persistence.entity.ReservationEntity;
import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;
import cz.muni.pa165.bookingmanager.persistence.entity.UserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

@ContextConfiguration(locations = { "/application-context.xml" } )
@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTest {

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

    @InjectMocks
    private ReservationService rs = new ReservationServiceImpl();


    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        user = new UserEntity();
        user.setAdmin(false);
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
        room.setHotel(hotel);
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
        assertEquals(r9nx,r9n);
    }

    @Test
    public void updateReservationTest(){
        when(reservationDao.save(r9n)).thenReturn(returnR9nWithSetEndDate(r9n));
        r9n.setEndDate(Date.valueOf("2016-09-02"));
        ReservationEntity r9nx = reservationDao.save(r9n);
        assertEquals(r9nx,r9n);
    }

    @Test
    public void findByIdNoneTest(){
        Optional<ReservationEntity> x = Optional.ofNullable(null);
        when(reservationDao.findOne(2L)).thenReturn(null);
        assertEquals(rs.findById(2L),x);
    }

    @Test
    public void findByIdExistsTest(){
        Optional<ReservationEntity> x = Optional.of(r9n);
        when(reservationDao.findOne(1L)).thenReturn(r9n);
        assertEquals(rs.findById(1L),x);
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
        when(reservationDao.findByOptionalCustomCriteria
                (f.getRoomId().get(),f.getCustomerId().get(),f.getStartsBefore().get(),f.getEndsAfter().get()
                        ,String.valueOf(f.getState().get()),prq)).thenReturn(elist);
        List<ReservationEntity> x = rs.findFiltered(f,info).getEntries();
        assertEquals(elist,x);
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
        Pageable prq = new PageRequest(1,10);
        PageInfo info = new PageInfo(1,10);
        List<ReservationEntity> y = rs.findFiltered(f,info).getEntries();
        assertEquals(y.size(),0);
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
