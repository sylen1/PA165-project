package cz.muni.pa165.bookingmanager.application.facade;

import cz.muni.pa165.bookingmanager.application.service.iface.RoomService;
import cz.muni.pa165.bookingmanager.iface.dto.RoomDto;
import cz.muni.pa165.bookingmanager.iface.facade.RoomFacade;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.RoomFilter;
import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;
import org.dozer.Mapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Gasior
 */
@ContextConfiguration(locations = { "/application-context.xml" } )
@RunWith(SpringJUnit4ClassRunner.class)
public class RoomFacadeTest {
    @Inject
    private ApplicationContext context;

    private RoomFacade roomFacade;

    private Mapper mapper;

    @PostConstruct
    public void init()
    {
        mapper = context.getBean(Mapper.class);
        RoomService roomService = mock(RoomService.class);
        RoomEntity nullable = null;
        when(roomService.findById(1L))
                .thenReturn(Optional.of(mapper.map(createRoom("find room 1"), RoomEntity.class)));

        when(roomService.findById(2L))
                .thenReturn(Optional.of(mapper.map(createRoom("find room 2"), RoomEntity.class)));

        when(roomService.findById(3L))
                .thenReturn(Optional.ofNullable(nullable));

        when(roomService.registerRoom(mapper.map(createRoom("create room"), RoomEntity.class)))
                .thenReturn(mapper.map(createRoom("create room"), RoomEntity.class));

        when(roomService.findByName(createRoom("find room 1").getName()))
                .thenReturn(Optional.of(mapper.map(createRoom("find room 1"), RoomEntity.class)));

        when(roomService.findByName(createRoom("find room 2").getName()))
                .thenReturn(Optional.of(mapper.map(createRoom("find room 2"), RoomEntity.class)));

        when(roomService.findByName(createRoom("not there").getName()))
                .thenReturn(Optional.ofNullable(nullable));


        List<RoomEntity> roomListFilter = new LinkedList<>();
        roomListFilter.add(mapper.map(createRoom("filtered 1"), RoomEntity.class));
        roomListFilter.add(mapper.map(createRoom("filtered 2"), RoomEntity.class));
        roomListFilter.add(mapper.map(createRoom("filtered 3"), RoomEntity.class));

        PageResult<RoomEntity> expectedPageFilter = new PageResult<>();
        expectedPageFilter.setEntries(roomListFilter);
        expectedPageFilter.setPageCount(1);
        expectedPageFilter.setPageNumber(0);
        expectedPageFilter.setTotalEntries(3);
        expectedPageFilter.setPageSize(10);

        when(roomService.filterRooms(new RoomFilter(1, 5, new BigDecimal(100), new BigDecimal(1000)), new PageInfo(0, 10)))
                .thenReturn(expectedPageFilter);

        List<RoomEntity> roomListAll = new LinkedList<>();
        roomListAll.add(mapper.map(createRoom("all 1"), RoomEntity.class));
        roomListAll.add(mapper.map(createRoom("all 2"), RoomEntity.class));
        roomListAll.add(mapper.map(createRoom("all 3"), RoomEntity.class));

        PageResult<RoomEntity> expectedPageAll = new PageResult<>();
        expectedPageAll.setEntries(roomListAll);
        expectedPageAll.setPageCount(1);
        expectedPageAll.setPageNumber(0);
        expectedPageAll.setTotalEntries(3);
        expectedPageAll.setPageSize(10);

        when(roomService.findAll(new PageInfo(0, 10))).thenReturn(mapPages(expectedPageAll));
        RoomEntity update = mapper.map(createRoom("update 1"), RoomEntity.class);
        update.setId(69L);
        update.setName("UpdatedName");
        when(roomService.updateRoom(update)).thenReturn(update);


        roomFacade = new RoomFacadeImpl(roomService, context.getBean(Mapper.class));
    }

    @Test
    public void findAllTest() {
        PageResult<RoomDto> actual = roomFacade.findAll(new PageInfo(0, 10));

        List<RoomDto> roomListAll = new LinkedList<>();
        roomListAll.add(createRoom("all 1"));
        roomListAll.add(createRoom("all 2"));
        roomListAll.add(createRoom("all 3"));

        PageResult<RoomDto> expected = new PageResult<>();
        expected.setEntries(roomListAll);
        expected.setTotalEntries(3);
        expected.setPageCount(1);
        expected.setPageNumber(0);
        expected.setPageSize(10);

        assertEquals(expected, actual);
    }

    @Test
    public void registerRoomTest() {
        RoomDto registered = roomFacade.registerRoom(createRoom("create room"));
        assertEquals(registered, createRoom("create room"));
    }

    @Test
    public void updateRoomTest() {
        RoomDto update = createRoom("update 1");

        update.setId(69L);
        update.setName("UpdatedName");

        assertEquals(update, roomFacade.updateRoom(update));
    }

    @Test
    public void findByNameTest() {
        Optional<RoomDto> byName = roomFacade.findByName(createRoom("find room 1").getName());
        assertTrue(byName.isPresent());
        assertEquals(createRoom("find room 1"), byName.get());

        byName = roomFacade.findByName(createRoom("find room 2").getName());
        assertTrue(byName.isPresent());
        assertEquals(createRoom("find room 2"), byName.get());

        byName = roomFacade.findByName(createRoom("not there").getName());
        assertFalse(byName.isPresent());
    }
    @Test
    public void findByIdTest() {
        Optional<RoomDto> byId = roomFacade.findById(1L);
        assertTrue(byId.isPresent());
        assertEquals(createRoom("find room 1"), byId.get());

        byId = roomFacade.findById(2L);
        assertTrue(byId.isPresent());
        assertEquals(createRoom("find room 2"), byId.get());

        byId = roomFacade.findById(3L);
        assertFalse(byId.isPresent());
    }

    @Test
    public void filterRoomsTets() {

        PageResult<RoomDto> actual = roomFacade.filterRooms(new RoomFilter(1, 5, new BigDecimal(100), new BigDecimal(1000)), new PageInfo(0, 10));

        List<RoomDto> roomList = new LinkedList<>();
        roomList.add(createRoom("filtered 1"));
        roomList.add(createRoom("filtered 2"));
        roomList.add(createRoom("filtered 3"));

        PageResult<RoomDto> expected = new PageResult<>();
        expected.setEntries(roomList);
        expected.setTotalEntries(3);
        expected.setPageCount(1);
        expected.setPageNumber(0);
        expected.setPageSize(10);

        assertEquals(expected, actual);

    }

    private RoomDto createRoom(String key){

            RoomDto roomDto = new RoomDto();
            roomDto.setName("A222_" + key);
            roomDto.setBedCount(key.length());
            roomDto.setDescription("Description of: " + roomDto.getName());
            roomDto.setPrice(new BigDecimal(roomDto.getDescription().length()*100));
            roomDto.setHotelId(1L);
            return roomDto;
    }

    private PageResult<RoomEntity> mapPages(PageResult<RoomEntity> page){
        List<RoomEntity> dtos = page.getEntries()
                .stream()
                .map(x -> mapper.map(x, RoomEntity.class))
                .collect(Collectors.toList());

        PageResult<RoomEntity> result = new PageResult<>();
        mapper.map(page, result);
        result.setEntries(dtos);

        return page;
    }
}
