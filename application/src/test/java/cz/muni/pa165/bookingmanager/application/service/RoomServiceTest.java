package cz.muni.pa165.bookingmanager.application.service;

/**
 * @author Gasior
 */

import cz.muni.pa165.bookingmanager.application.service.iface.RoomService;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.RoomFilter;
import cz.muni.pa165.bookingmanager.persistence.dao.RoomDao;
import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;
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
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(locations = { "/application-context.xml" } )
@RunWith(SpringJUnit4ClassRunner.class)
public class RoomServiceTest {

    @Inject
    private ApplicationContext context;

    private RoomService roomService;

    @PostConstruct
    public void init()
    {
        RoomDao roomDao = mock(RoomDao.class);

        when(roomDao.findOne(1L))
                .thenReturn(createRoom("find room 1"));

        when(roomDao.findOne(2L))
                .thenReturn(createRoom("find room 2"));

        when(roomDao.findOne(3L))
                .thenReturn(null);

        when(roomDao.save(createRoom("create room")))
                .thenReturn(createRoom("create room"));

        when(roomDao.findByName(createRoom("find room 1").getName()))
                .thenReturn(createRoom("find room 1"));

        when(roomDao.findByName(createRoom("find room 2").getName()))
                .thenReturn(createRoom("find room 2"));

        when(roomDao.findByName(createRoom("not there").getName()))
                .thenReturn(null);

        RoomEntity update = createRoom("update 1");

        update.setId(69L);
        update.setName("UpdatedName");

        when(roomDao.save(update)).thenReturn(update);



        List<RoomEntity> roomList = new LinkedList<>();
        roomList.add(createRoom("filtered 1"));
        roomList.add(createRoom("filtered 2"));
        roomList.add(createRoom("filtered 3"));
        when(roomDao.findByBedCountGreaterThanAndBedCountLessThanAndPriceGreaterThanAndPriceLessThan(1, 5, new BigDecimal(100), new BigDecimal(1000), new PageRequest(0, 10))).thenReturn(new PageImpl<>(roomList, new PageRequest(0,10), 3));

        List<RoomEntity> roomListAll = new LinkedList<>();
        roomListAll.add(createRoom("all 1"));
        roomListAll.add(createRoom("all 2"));
        roomListAll.add(createRoom("all 3"));
        when(roomDao.findAll(new PageRequest(0, 10))).thenReturn(new PageImpl<>(roomListAll, new PageRequest(0,10), 3));

        when(roomDao.findByName(createRoom("not there").getName()))
                .thenReturn(null);

        roomService = new RoomServiceImpl(roomDao, context.getBean(Mapper.class));
    }

    @Test
    public void updateRoomTest() {
        RoomEntity update = createRoom("update 1");

        update.setId(69L);
        update.setName("UpdatedName");

        assertEquals(update, roomService.updateRoom(update));
    }

    @Test
    public void findByIdTest()
    {
        Optional<RoomEntity> byId = roomService.findById(1L);
        assertTrue(byId.isPresent());
        assertEquals(createRoom("find room 1"), byId.get());

        byId = roomService.findById(2L);
        assertTrue(byId.isPresent());
        assertEquals(createRoom("find room 2"), byId.get());

        byId = roomService.findById(3L);
        assertFalse(byId.isPresent());
    }

    @Test
    public void findByNameTest()
    {
        Optional<RoomEntity> byName = roomService.findByName(createRoom("find room 1").getName());
        assertTrue(byName.isPresent());
        assertEquals(createRoom("find room 1"), byName.get());

        byName = roomService.findByName(createRoom("find room 2").getName());
        assertTrue(byName.isPresent());
        assertEquals(createRoom("find room 2"), byName.get());

        byName = roomService.findByName(createRoom("not there").getName());
        assertFalse(byName.isPresent());
    }

    @Test
    public void registerRoomTest()
    {
        RoomEntity registered = roomService.registerRoom(createRoom("create room"));
        assertEquals(registered, createRoom("create room"));
    }
    @Test
    public void findAllRoomsTest()
    {
        PageResult<RoomEntity> actual = roomService.findAll(new PageInfo(0, 10));

        List<RoomEntity> roomListAll = new LinkedList<>();
        roomListAll.add(createRoom("all 1"));
        roomListAll.add(createRoom("all 2"));
        roomListAll.add(createRoom("all 3"));

        PageResult<RoomEntity> expected = new PageResult<>();
        expected.setEntries(roomListAll);
        expected.setTotalEntries(3);
        expected.setPageCount(1);
        expected.setPageNumber(0);
        expected.setPageSize(10);

        assertEquals(expected, actual);
    }


    @Test
    public void filterRoomsTest()
    {
        PageResult<RoomEntity> actual = roomService.filterRooms(new RoomFilter(1, 5, new BigDecimal(100), new BigDecimal(1000)), new PageInfo(0, 10));

        List<RoomEntity> roomList = new LinkedList<>();
        roomList.add(createRoom("filtered 1"));
        roomList.add(createRoom("filtered 2"));
        roomList.add(createRoom("filtered 3"));

        PageResult<RoomEntity> expected = new PageResult<>();
        expected.setEntries(roomList);
        expected.setTotalEntries(3);
        expected.setPageCount(1);
        expected.setPageNumber(0);
        expected.setPageSize(10);

        assertEquals(expected, actual);
    }

    private RoomEntity createRoom(String key){

            RoomEntity roomEntity = new RoomEntity();
            roomEntity.setName("A222_" + key);
            roomEntity.setBedCount(key.length());
            roomEntity.setDescription("Description of: " + roomEntity.getName());
            roomEntity.setPrice(new BigDecimal(roomEntity.getDescription().length() * 100));
            return roomEntity;
        }
}
