package cz.muni.pa165.bookingmanager.iface.facade;

import cz.muni.pa165.bookingmanager.iface.dto.RoomDto;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.RoomFilter;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RoomFacade extends PageableFacade<RoomDto> {
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
    public PageResult<RoomDto> filterRooms(RoomFilter filter, PageInfo pageInfo);

    /**
     * Returns page of rooms, which don't have any reservation in given range of dates, optionally filtered by given
     * restrictions for properties of a room and by city of the hotel of a room.
     * Takes into account reservations of all statuses except CANCELLED.
     *
     * @param availableFrom inclusive start date of the interval, in which given rooms must be available for reservation
     * @param availableTo inclusive end date of the interval, in which given rooms must be available for reservation
     * @param roomPropertyRestrictions instance of RoomFilter for specifying range for properties of returned rooms.
     * @param city name of city, in which returned rooms have to be located. Empty string means no restriction.
     * @param pageInfo contains pagination parameters
     * @return page of rooms meeting the criteria from arguments of this method.
     */
    PageResult<RoomDto> findAvailableRooms(Date availableFrom, Date availableTo, RoomFilter roomPropertyRestrictions,
                                           String city, PageInfo pageInfo);
}
