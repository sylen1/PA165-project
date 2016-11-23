package cz.muni.pa165.bookingmanager.iface.facade;

import cz.muni.pa165.bookingmanager.iface.dto.RoomDto;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.RoomFilter;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;

import java.util.Optional;

public interface RoomFacade {
    /**
     * Lists all rooms
     * @param pageInfo page info
     * @return rooms
     */
    public PageResult<RoomDto> findAll(PageInfo pageInfo);

    /**
     * Registers new room
     * @param roomDto new room
     * @return room
     */
    public RoomDto registerRoom(RoomDto roomDto);

    /**
     * Updates existing room
     * @param roomDto updated room
     * @return room
     */
    public RoomDto updateRoom(RoomDto roomDto) ;

    /**
     * finds room by name
     * @param name name of room
     * @return room
     */
    public Optional<RoomDto> findByName(String name) ;

    /**
     * finds room by id
     * @param id unique id
     * @return room
     */
    public Optional<RoomDto> findById(Long id);

    /**
     * Returns filtered rooms
     * @param filter specifies room criterias
     * @param pageInfo page info
     * @return filtered rooms
     */
    public PageResult<RoomDto> filterRooms(RoomFilter filter, PageInfo pageInfo) ;


}
