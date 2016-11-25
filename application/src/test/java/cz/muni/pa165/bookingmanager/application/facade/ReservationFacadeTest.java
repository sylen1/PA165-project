package cz.muni.pa165.bookingmanager.application.facade;

import cz.muni.pa165.bookingmanager.application.service.ReservationServiceImpl;
import cz.muni.pa165.bookingmanager.application.service.iface.ReservationService;
import cz.muni.pa165.bookingmanager.iface.dto.*;
import cz.muni.pa165.bookingmanager.iface.facade.ReservationFacade;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.ReservationFilter;
import cz.muni.pa165.bookingmanager.persistence.dao.HotelDao;
import cz.muni.pa165.bookingmanager.persistence.dao.ReservationDao;
import cz.muni.pa165.bookingmanager.persistence.dao.RoomDao;
import cz.muni.pa165.bookingmanager.persistence.dao.UserDao;
import cz.muni.pa165.bookingmanager.persistence.entity.HotelEntity;
import cz.muni.pa165.bookingmanager.persistence.entity.ReservationEntity;
import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;
import cz.muni.pa165.bookingmanager.persistence.entity.UserEntity;
import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Reservation facade tests
 * @author Matej Harcar, 422714
 */
@ContextConfiguration(locations = { "/application-context.xml" } )
@RunWith(SpringJUnit4ClassRunner.class)
public class ReservationFacadeTest {

    private ReservationFacade rf;

    @Mock
    private ReservationService rs;

    private UserDto user;
    private HotelDto hotel;
    private RoomDto room;


    @Inject
    private Mapper mapper;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        rf = new ReservationFacadeImpl(rs, mapper);

        user = new UserDto();
        user.setAdmin(false);
        user.setId(1L);
        user.setEmail("user@mail.com");
        user.setAddress("Asdf 5, 32 768 Qwertyville");
        user.setPhoneNumber("3874619786317896");
        user.setName("Hjbdc Kkv");
        user.setBirthDate(Date.valueOf("1998-05-09"));
        user.setPasswordSalt(new byte[]{0x3f, 0x5e, 0x1a});
        user.setPasswordHash(new byte[]{0x66, 0x78, (byte) 0x9c});

        hotel = new HotelDto();
        hotel.setEmail("hotel@mail.com");
        hotel.setPhoneNumber("430721856785");
        hotel.setStreetNumber("64C");
        hotel.setCity("Fooville");
        hotel.setStreet("Barstreet");
        hotel.setName("Foobar Hotel");
        hotel.setId(1L);

        room = new RoomDto();
        room.setBedCount(4);
        room.setDescription("Nondescript room");
        room.setId(1L);
        room.setPrice(new BigDecimal("10.24"));
        room.setName("A32");
        room.setHotel(hotel);

    }

    @Test
    public void createReservationTest(){
        ReservationDto r1 = new ReservationDto();
        r1.setCustomer(user);
        r1.setRoom(room);
        r1.setState(ReservationState.NEW);
        r1.setStartDate(new java.util.Date(java.sql.Date.valueOf("2016-07-25").getTime()));
        r1.setEndDate(new java.util.Date(java.sql.Date.valueOf("2016-08-15").getTime()));
        ReservationEntity r1e = mapper.map(r1,ReservationEntity.class);
        when(rs.createReservation(r1e)).thenReturn(giveR9nId(r1e));
        r1 = rf.createReservation(r1);
        assertNotNull(r1);
        assertEquals(user,r1.getCustomer());
        assertEquals(room,r1.getRoom());
        assertEquals(ReservationState.NEW,r1.getState());
        assertEquals(Date.valueOf("2016-07-25"),r1.getStartDate());
        assertEquals(Date.valueOf("2016-08-15"),r1.getEndDate());
    }

    @Test
    public void updateReservationTest(){
        ReservationDto r1 = new ReservationDto();
        r1.setCustomer(user);
        r1.setRoom(room);
        r1.setState(ReservationState.NEW);
        r1.setStartDate(Date.valueOf("2016-07-25"));
        r1.setEndDate(Date.valueOf("2016-08-15"));
        ReservationEntity r1e = mapper.map(r1,ReservationEntity.class);
        when(rs.createReservation(r1e)).thenReturn(giveR9nId(r1e));
        r1 = rf.createReservation(r1);
        r1e.setId(r1.getId());

        Long id = r1.getId();
        r1.setEndDate(Date.valueOf("2016-08-13"));
        when(rs.updateReservation(r1e)).thenReturn(changeR9n(r1e));
        r1 = rf.updateReservation(r1);
        assertEquals(id,r1.getId());
        assertEquals(Date.valueOf("2016-08-13"),r1.getEndDate());
    }


    @Test
    public void findByIdTest(){
        ReservationDto r1 = new ReservationDto();
        r1.setCustomer(user);
        r1.setRoom(room);
        r1.setState(ReservationState.NEW);
        r1.setStartDate(Date.valueOf("2016-07-25"));
        r1.setEndDate(Date.valueOf("2016-08-15"));
        ReservationEntity r1e = mapper.map(r1,ReservationEntity.class);
        when(rs.createReservation(r1e)).thenReturn(giveR9nId(r1e));
        r1 = rf.createReservation(r1);
        r1e.setId(r1.getId());
        when(rs.findById(r1e.getId())).thenReturn(Optional.of(r1e));
        Long id = r1.getId();

        Optional<ReservationDto> x = rf.findById(id);
        assertTrue(x.isPresent());
        assertEquals(r1,x.get());
    }

    @Test
    public void findAllTest(){
        ReservationDto r1 = new ReservationDto();
        r1.setCustomer(user);
        r1.setRoom(room);
        r1.setState(ReservationState.NEW);
        r1.setStartDate(Date.valueOf("2016-07-25"));
        r1.setEndDate(Date.valueOf("2016-08-15"));

        ReservationDto r2 = new ReservationDto();
        r2.setCustomer(user);
        r2.setRoom(room);
        r2.setState(ReservationState.NEW);
        r2.setStartDate(Date.valueOf("2016-06-05"));
        r2.setEndDate(Date.valueOf("2016-06-10"));

        ReservationEntity r1e = mapper.map(r1,ReservationEntity.class);
        when(rs.createReservation(r1e)).thenReturn(giveR9nId(r1e));
        ReservationEntity r2e = mapper.map(r2,ReservationEntity.class);
        when(rs.createReservation(r2e)).thenReturn(giveR9nId(r2e));
        r1 = rf.createReservation(r1);
        r2 = rf.createReservation(r2);
        r1e.setId(r1.getId());
        r2e.setId(r2.getId());
        List<ReservationDto> rl = new ArrayList<>();
        rl.add(r1);rl.add(r2);

        PageInfo i = new PageInfo(0, 10);
        PageResult<ReservationDto> a = new PageResult<>();
        a.setPageCount(1);
        a.setPageNumber(i.getPageNumber());
        a.setPageSize(i.getPageSize());
        a.setTotalEntries(2);
        a.setEntries(rl);

        List<ReservationEntity> rel = new ArrayList<>();
        rel.add(r1e);rel.add(r2e);
        PageResult<ReservationEntity> ep = new PageResult<>();
        ep.setEntries(rel);
        ep.setPageCount(1);
        ep.setPageNumber(i.getPageNumber());
        ep.setPageSize(i.getPageSize());
        ep.setTotalEntries(2);

        when(rs.findAll(i)).thenReturn(ep);
        PageResult<ReservationDto> x = rf.findAll(i);
        assertEquals(a,x);
    }

    @Test
    public void findFilteredTest(){
        ReservationDto r1 = new ReservationDto();
        r1.setCustomer(user);
        r1.setRoom(room);
        r1.setState(ReservationState.NEW);
        r1.setStartDate(Date.valueOf("2016-07-25"));
        r1.setEndDate(Date.valueOf("2016-08-15"));
        ReservationEntity r1e = mapper.map(r1,ReservationEntity.class);
        when(rs.createReservation(r1e)).thenReturn(giveR9nId(r1e));
        r1 = rf.createReservation(r1);
        r1e.setId(r1.getId());
        ReservationFilter f = new ReservationFilter();
        f.setState(ReservationState.NEW);
        f.setCustomerId(user.getId());
        f.setRoomId(room.getId());
        f.setStartsBefore(Date.valueOf("2016-07-26"));
        f.setEndsAfter(Date.valueOf("2016-08-13"));

        List<ReservationDto> rl = new ArrayList<>();
        rl.add(r1);

        PageInfo i = new PageInfo(0, 10);
        PageResult<ReservationDto> a = new PageResult<>();
        a.setPageCount(1);
        a.setPageNumber(i.getPageNumber());
        a.setPageSize(i.getPageSize());
        a.setTotalEntries(1);
        a.setEntries(rl);

        List<ReservationEntity> rle = new ArrayList<>();
        rle.add(r1e);
        PageResult<ReservationEntity> ep = new PageResult<>();
        ep.setEntries(rle);
        ep.setPageCount(1);
        ep.setPageNumber(i.getPageNumber());
        ep.setPageSize(i.getPageSize());
        ep.setTotalEntries(1);

        when(rs.findFiltered(f,i)).thenReturn(ep);
        PageResult<ReservationDto> x = rf.findFiltered(f,i);
        assertEquals(a,x);
    }

    private ReservationEntity giveR9nId(ReservationEntity re) {
        re.setId(1L);
        return re;
    }

    private ReservationEntity changeR9n(ReservationEntity r1e) {
        r1e.setEndDate(Date.valueOf("2016-08-13"));
        return r1e;
    }
}
