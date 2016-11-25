package cz.muni.pa165.bookingmanager.application.service;

import cz.muni.pa165.bookingmanager.application.service.iface.HotelService;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.persistence.dao.HotelDao;
import cz.muni.pa165.bookingmanager.persistence.entity.HotelEntity;
import org.dozer.Mapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(locations = { "/application-context.xml" } )
@RunWith(SpringJUnit4ClassRunner.class)
public class HotelServiceTest {
    @Inject
    private ApplicationContext context;

    private HotelService hotelService;

    @PostConstruct
    public void init(){
        hotelCache = new HashMap<>();
        HotelDao hotelDao = mock(HotelDao.class);

        when(hotelDao.findOne(1L))
                .thenReturn(createHotel("find H1"));

        when(hotelDao.findOne(2L))
                .thenReturn(createHotel("find H2"));

        when(hotelDao.findOne(3L))
                .thenReturn(null);

        when(hotelDao.save(createHotel("create H")))
                .thenReturn(createHotel("create H"));

        List<HotelEntity> hotels = new LinkedList<>();
        hotels.add(createHotel("city H1"));
        hotels.add(createHotel("city H2"));
        hotels.add(createHotel("city H3"));
        when(hotelDao.findByCity("Brno", new PageRequest(0, 10)))
                .thenReturn(new PageImpl<>(hotels, new PageRequest(0, 10), 3));

        when(hotelDao.save(createHotel("update H1")))
                .thenReturn(createHotel("update H1"));

        List<HotelEntity> allHotels = new LinkedList<>();
        allHotels.add(createHotel("all H1"));
        allHotels.add(createHotel("all H2"));
        allHotels.add(createHotel("all H3"));
        allHotels.add(createHotel("all H4"));
        allHotels.add(createHotel("all H5"));
        when(hotelDao.findAll(new PageRequest(0, 10)))
                .thenReturn(new PageImpl<>(allHotels, new PageRequest(0, 10), 5));

        hotelService = new HotelServiceImpl(hotelDao, context.getBean(Mapper.class));
    }

    @Test
    public void findByIdTest(){
        Optional<HotelEntity> byId = hotelService.findById(1L);
        assertTrue(byId.isPresent());
        assertEquals(createHotel("find H1"), byId.get());

        byId = hotelService.findById(2L);
        assertTrue(byId.isPresent());
        assertEquals(createHotel("find H2"), byId.get());

        byId = hotelService.findById(3L);
        assertFalse(byId.isPresent());
    }

    @Test
    public void registerHotelTest(){
        HotelEntity registered = hotelService.registerHotel(createHotel("create H"));
        assertEquals(registered, createHotel("create H"));
    }

    @Test
    public void findByCityTest(){
        PageResult<HotelEntity> actual = hotelService.findByCity("Brno", new PageInfo(0, 10));

        List<HotelEntity> hotels = new LinkedList<>();
        hotels.add(createHotel("city H1"));
        hotels.add(createHotel("city H2"));
        hotels.add(createHotel("city H3"));

        PageResult<HotelEntity> expected = new PageResult<>();
        expected.setEntries(hotels);
        expected.setTotalEntries(3);
        expected.setPageCount(1);
        expected.setPageNumber(0);
        expected.setPageSize(10);

        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void updateHotelTest(){
        HotelEntity hotel1 = createHotel("update H1");
        hotel1.setId(1L);
        HotelEntity returned = hotelService.updateHotelInformation(hotel1);
        assertEquals(hotel1, returned);

        HotelEntity hotelNull = createHotel("update H null");
        hotelNull.setId(null);
        hotelService.updateHotelInformation(hotelNull);
    }

    @Test
    public void findAllTest(){
        List<HotelEntity> allHotels = new LinkedList<>();
        allHotels.add(createHotel("all H1"));
        allHotels.add(createHotel("all H2"));
        allHotels.add(createHotel("all H3"));
        allHotels.add(createHotel("all H4"));
        allHotels.add(createHotel("all H5"));

        PageResult<HotelEntity> expected = new PageResult<>();
        expected.setEntries(allHotels);
        expected.setPageSize(10);
        expected.setPageNumber(0);
        expected.setPageCount(1);
        expected.setTotalEntries(5);

        PageResult<HotelEntity> actual = hotelService.findAll(new PageInfo(0, 10));
        assertEquals(expected, actual);
    }

    private HotelEntity createHotel(String seed){
        HotelEntity hotel = hotelCache.get(seed);

        if (hotel == null){
            hotel = new HotelEntity();
            hotel.setName("name: " + seed);
            hotel.setCity("Brno");
            hotel.setEmail(seed + "@hotel.com");
            hotel.setPhoneNumber("number: " + seed);
            hotel.setStreet("streed: " + seed);
            hotel.setStreetNumber("st number: " + seed);
            hotelCache.put(seed, hotel);
        }

        return hotel;
    }
    private Map<String, HotelEntity> hotelCache;
}
