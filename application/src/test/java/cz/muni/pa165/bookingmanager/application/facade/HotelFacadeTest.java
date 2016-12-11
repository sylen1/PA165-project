package cz.muni.pa165.bookingmanager.application.facade;

import cz.muni.pa165.bookingmanager.application.service.iface.HotelService;
import cz.muni.pa165.bookingmanager.iface.dto.HotelDto;
import cz.muni.pa165.bookingmanager.iface.facade.HotelFacade;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.persistence.entity.HotelEntity;
import org.dozer.Mapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
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
public class HotelFacadeTest {
    @Inject
    private ApplicationContext context;

    private HotelFacade hotelFacade;
    private Mapper mapper;

    @PostConstruct
    public void init(){
        hotelCache = new HashMap<>();
        HotelService hotelService = mock(HotelService.class);

        when(hotelService.findById(1L))
                .thenReturn(Optional.of(createHotel("create H1")));

        when(hotelService.findById(2L))
                .thenReturn(Optional.of(createHotel("create H2")));

        when(hotelService.findById(3L))
                .thenReturn(Optional.empty());

        List<HotelEntity> hotels = new LinkedList<>();
        hotels.add(createHotel("city H"));
        PageResult<HotelEntity> pageResult = new PageResult<>();
        pageResult.setTotalEntries(1);
        pageResult.setPageCount(1);
        pageResult.setPageNumber(0);
        pageResult.setPageSize(10);
        pageResult.setEntries(hotels);

        when(hotelService.findByCity("Brno", new PageInfo(0, 10)))
                .thenReturn(pageResult);

        HotelEntity registerHotel = createHotel("register H");
        registerHotel.setId(1L);
        when(hotelService.registerHotel(createHotel("register H")))
                .thenReturn(registerHotel);

        List<HotelEntity> allHotels = new LinkedList<>();
        allHotels.add(createHotel("all H1"));
        allHotels.add(createHotel("all H2"));
        allHotels.add(createHotel("all H3"));
        allHotels.add(createHotel("all H4"));
        PageResult<HotelEntity> allHotelsPage = new PageResult<>();
        allHotelsPage.setPageSize(10);
        allHotelsPage.setTotalEntries(4);
        allHotelsPage.setPageCount(1);
        allHotelsPage.setPageNumber(0);
        allHotelsPage.setEntries(allHotels);

        when(hotelService.findAll(new PageInfo(0, 10)))
                .thenReturn(allHotelsPage);

        HotelEntity hotelEntity = createHotel("update H");
        hotelEntity.setId(1L);
        when(hotelService.updateHotelInformation(hotelEntity))
                .thenReturn(hotelEntity);

        mapper = context.getBean(Mapper.class);
        hotelFacade = new HotelFacadeImpl(hotelService, mapper);
    }

    @Test
    public void findByIdTest(){
        Optional<HotelDto> byId1 = hotelFacade.findById(1L);
        assertTrue(byId1.isPresent());
        assertEquals(createHotel("create H1"), byId1.map(x -> mapper.map(x, HotelEntity.class)).get());

        Optional<HotelDto> byId2 = hotelFacade.findById(2L);
        assertTrue(byId2.isPresent());
        assertEquals(createHotel("create H2"), byId2.map(x -> mapper.map(x, HotelEntity.class)).get());

        Optional<HotelDto> byId3 = hotelFacade.findById(3L);
        assertFalse(byId3.isPresent());
    }

    @Test
    public void findByCityTest(){
        List<HotelDto> hotels = new LinkedList<>();
        hotels.add(mapper.map(createHotel("city H"), HotelDto.class));
        PageResult<HotelDto> expected = new PageResult<>();
        expected.setEntries(hotels);
        expected.setPageSize(10);
        expected.setPageNumber(0);
        expected.setPageCount(1);
        expected.setTotalEntries(1);

        PageResult<HotelDto> actual = hotelFacade.findByCity("Brno", new PageInfo(0, 10));
        assertEquals(expected, actual);
    }

    @Test
    public void registerHotelTest(){
        HotelDto expected = mapper.map(createHotel("register H"), HotelDto.class);
        expected.setId(1L);

        HotelDto hotel = mapper.map(createHotel("register H"), HotelDto.class);
        hotel.setId(null);
        HotelDto actual = hotelFacade.registerHotel(hotel);

        assertEquals(expected, actual);
        assertEquals(Long.valueOf(1L), actual.getId());
    }

    @Test
    public void findAllTest(){
        List<HotelDto> allHotels = new LinkedList<>();
        allHotels.add(mapper.map(createHotel("all H1"), HotelDto.class));
        allHotels.add(mapper.map(createHotel("all H2"), HotelDto.class));
        allHotels.add(mapper.map(createHotel("all H3"), HotelDto.class));
        allHotels.add(mapper.map(createHotel("all H4"), HotelDto.class));
        PageResult<HotelDto> expected = new PageResult<>();
        expected.setPageSize(10);
        expected.setTotalEntries(4);
        expected.setPageCount(1);
        expected.setPageNumber(0);
        expected.setEntries(allHotels);

        PageResult<HotelDto> actual = hotelFacade.findAll(new PageInfo(0, 10));
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void updateHotelTest(){
        HotelDto expected = mapper.map(createHotel("update H"), HotelDto.class);
        expected.setPhoneNumber(expected.getPhoneNumber() + " updated");
        expected.setId(1L);

        HotelDto actual = hotelFacade.updateHotelInformation(expected);
        assertEquals(expected, actual);

        HotelDto hotelNull = mapper.map(createHotel("update H null"), HotelDto.class);
        hotelNull.setId(null);
        hotelFacade.updateHotelInformation(hotelNull);
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
