package cz.muni.pa165.bookingmanager.persistence.dao;

import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Ludovit Labaj
 * */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/persistence-context.xml")
public class RoomDaoTest {
    private static final Logger LOG = LoggerFactory.getLogger(RoomDaoTest.class);

    private int roomCounter;

    @Inject
    private RoomDao roomDao;

    @Before
    public void clearDb(){
        roomDao.deleteAll();
        assertEquals(0, roomDao.count());
    }

    @Test
    public void createRoomTest(){
        RoomEntity nextRoom = nextRoom();
        assertNull(nextRoom.getId());

        RoomEntity savedRoom = roomDao.save(nextRoom);
        assertNotNull(savedRoom.getId());

        assertEquals(1, roomDao.count());
    }
    @Test
    public void filteredTest() {
        for(int i = 0; i < 5; i++) {
            roomDao.save(nextRoom());
        }
        List<RoomEntity> roomEntities =  roomDao.findByBedCountGreaterThanAndBedCountLessThanAndPriceGreaterThanAndPriceLessThan(0,1000, new BigDecimal(0), new BigDecimal(10000), new PageRequest(0, 5));
        int i = roomDao.countByBedCountGreaterThanAndBedCountLessThanAndPriceGreaterThanAndPriceLessThan(0,1000, new BigDecimal(0), new BigDecimal(10000));
        assertTrue(i == 5);
        assertTrue(roomDao.count() == 5);
        assertTrue(0 < roomEntities.size());
    }
    @Test
    public void deleteRoomTest(){
        RoomEntity nextRoom = nextRoom();
        RoomEntity savedRoom = roomDao.save(nextRoom);
        assertEquals(1, roomDao.count());

        roomDao.delete(savedRoom);
        assertEquals(0, roomDao.count());
    }

    @Test
    public void deleteRoomByIdTest(){
        RoomEntity nextRoom = nextRoom();
        RoomEntity savedRoom = roomDao.save(nextRoom);
        assertEquals(1, roomDao.count());

        roomDao.delete(savedRoom.getId());
        assertEquals(0, roomDao.count());
    }

    @Test
    public void deleteAllRoomsTest(){
        roomDao.save(nextRoom());
        roomDao.save(nextRoom());
        roomDao.save(nextRoom());
        assertEquals(3, roomDao.count());

        roomDao.deleteAll();
        assertEquals(0, roomDao.count());
    }

    @Test
    public void readAllRoomsTest(){
        roomDao.save(nextRoom());
        roomDao.save(nextRoom());
        roomDao.save(nextRoom());
        assertEquals(3, roomDao.count());

        List<RoomEntity> allRooms = roomDao.findAll();
        assertEquals(3, allRooms.size());
    }

    @Test
    public void readRoomByIdTest(){
        roomDao.save(nextRoom());
        RoomEntity originalRoom = nextRoom();
        originalRoom = roomDao.save(originalRoom);
        roomDao.save(nextRoom());
        assertEquals(3, roomDao.count());

        RoomEntity foundRoom = roomDao.findOne(originalRoom.getId());
        assertEquals(originalRoom.getId(), foundRoom.getId());
    }

    @Test
    public void updateRoomTest(){
        RoomEntity originalRoom = nextRoom();
        originalRoom = roomDao.save(originalRoom);
        assertEquals(1, roomDao.count());

        RoomEntity updatedRoom = new RoomEntity();
        updatedRoom.setId(originalRoom.getId());
        updatedRoom.setName(originalRoom.getName());
        updatedRoom.setPrice(originalRoom.getPrice());
        updatedRoom.setHotel(originalRoom.getHotel());
        updatedRoom.setBedCount(originalRoom.getBedCount());
        updatedRoom.setDescription(originalRoom.getDescription() + "UPDATED");

        roomDao.save(updatedRoom);
        assertEquals(1, roomDao.count());

        RoomEntity foundRoom = roomDao.findOne(updatedRoom.getId());
        assertEquals(updatedRoom.getDescription(), foundRoom.getDescription());
    }

    @Test
    public void readNonexistentTest(){
        assertEquals(0, roomDao.count());

        RoomEntity foundRoom = roomDao.findOne(1L);
        assertNull(foundRoom);
    }

    private RoomEntity nextRoom(){
        RoomEntity room = new RoomEntity();
        room.setName("name" + roomCounter);
        room.setBedCount(2);
        room.setDescription("Room description " + roomCounter);
        room.setHotel(null);
        room.setPrice(BigDecimal.TEN.add(BigDecimal.valueOf(roomCounter)));

        roomCounter++;
        return room;
    }
}
