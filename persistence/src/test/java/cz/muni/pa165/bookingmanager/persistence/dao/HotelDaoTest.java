package cz.muni.pa165.bookingmanager.persistence.dao;

import cz.muni.pa165.bookingmanager.persistence.entity.HotelEntity;
import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

/**
 * @Author Ondrej Gasior
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/persistence-context.xml")
public class HotelDaoTest {
    private static final Logger LOG = LoggerFactory.getLogger(HotelDaoTest.class);

    @Inject
    private HotelDao hotelDao;

    @Inject
    private RoomDao roomDao;

    @Before
    public void clean() {
        roomDao.deleteAll();
        hotelDao.deleteAll();
        assertEquals(0, hotelDao.count());
    }

    @Test
    public void createHotelTest() {


        int i = 0;
        for (HotelEntity hotel : getSomeHotels(3)) {
            assertNull(hotel.getId());
            assertNotNull(hotelDao.save(hotel).getId());
            assertEquals(++i, hotelDao.count());
        }


    }

    @Test
    public void createHotelWithRoomsTest(){
        HotelEntity hotel = getSomeHotels(1).get(0);
        hotel = hotelDao.save(hotel);

        Set<RoomEntity> rooms = getSomeRooms(5, hotel);
        roomDao.save(rooms);

        HotelEntity savedHotel = hotelDao.findOne(hotel.getId());
        assertEquals(rooms.size(), savedHotel.getRooms().size());
        assertEquals(rooms, savedHotel.getRooms());
    }

    @Test
    public void deleteHotelTest() {

        List<HotelEntity> savedHotels = new ArrayList<>();
        for (HotelEntity hotel : getSomeHotels(3)) {
            assertNull(hotel.getId());
            savedHotels.add(hotelDao.save(hotel));
        }
        assertEquals(savedHotels.size(), hotelDao.count());

        hotelDao.delete(savedHotels);
        assertEquals(0, hotelDao.count());
    }

    @Test
    public void deleteHotelByIdTest() {
        HotelEntity hotel = getSomeHotels(1).get(0);
        HotelEntity savedHotel = hotelDao.save(hotel);
        assertEquals(1, hotelDao.count());

        hotelDao.delete(savedHotel.getId());
        assertEquals(0, hotelDao.count());
    }

    @Test
    public void deleteAllHotelsTest() {

        hotelDao.save(getSomeHotels(3));
        assertEquals(3, hotelDao.count());

        hotelDao.deleteAll();
        assertEquals(0, hotelDao.count());
    }

    @Test
    public void readAllHotelsTest() {
        for (HotelEntity hotel : getSomeHotels(3)) {
            hotelDao.save(hotel);
        }
        assertEquals(3, hotelDao.count());

        List<HotelEntity> hotels = hotelDao.findAll();
        assertEquals(3, hotels.size());
    }

    @Test
    public void readHotelByIdTest() {

        hotelDao.save(getSomeHotels(1).get(0));
        HotelEntity hotelEntity = hotelDao.save(getSomeHotels(2).get(1));
        hotelDao.save(getSomeHotels(3).get(2));
        assertEquals(3, hotelDao.count());
        String s = new Date().toString();
        HotelEntity foundHotel = hotelDao.findOne(hotelEntity.getId());
        assertEquals(hotelEntity.getId(), foundHotel.getId());
    }

    @Test
    public void updateHotelTest() {
        HotelEntity hotelEntity = getSomeHotels(1).get(0);
        hotelEntity = hotelDao.save(hotelEntity);
        assertEquals(1, hotelDao.count());

        HotelEntity hotel = new HotelEntity();
        hotel.setId(hotelEntity.getId());
        hotel.setName(new Date().toString() + " Hotel n");
        hotel.setCity(new Date().toString() + " Gotham ");
        hotel.setEmail((new Date().toString() + " @hotel.com"));
        hotel.setPhoneNumber(new Date().toString() + " 123 456 789");
        hotel.setStreet(new Date().toString() + " ChampsElysess");
        hotel.setStreetNumber(new Date().toString() + " 4");

        hotelDao.save(hotel);
        assertEquals(1, hotelDao.count());

        HotelEntity updateHotel = hotelDao.findOne(hotel.getId());
        assertEquals(updateHotel, hotel);
    }

    private List<HotelEntity> getSomeHotels(int count) {
        List<HotelEntity> hotelEntities = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            HotelEntity hotel = new HotelEntity();
            hotel.setName(new Date().toString() + " Hotel n" + (i + 1));
            hotel.setCity("Gotham " + (i + 1));
            hotel.setEmail((i + 1) + "@hotel.com");
            hotel.setPhoneNumber((i + 1) + "123 456 789");
            hotel.setStreet((i + 1) + "ChampsElysess");
            hotel.setStreetNumber("4" + ((i + i + 1) * (i + 10)));
            hotelEntities.add(hotel);
        }
        return hotelEntities;
    }

    private Set<RoomEntity> getSomeRooms(int count, HotelEntity hotel) {
        Set<RoomEntity> rooms = new HashSet<>();
        for (int i = 0; i < count; i++){
            RoomEntity room = new RoomEntity();
            room.setName("Room" + i);
            room.setPrice(BigDecimal.TEN);
            room.setBedCount(2);
            room.setHotelId(hotel.getId());

            rooms.add(room);
        }

        return rooms;
    }

}