package cz.muni.pa165.bookingmanager.application.service.iface;

import cz.muni.pa165.bookingmanager.iface.util.RoomFilter;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Gasior
 */
public interface RoomService extends PageableService<RoomEntity> {
    /**
     * Registers new room to the system
     * @param roomEntity room to be saved
     * @return saved room instance
     */
    RoomEntity registerRoom(RoomEntity roomEntity);

    /**
     * Updates informations about exiting room
     * @param roomEntity room to be updated
     * @return updated room entity
     */
    RoomEntity updateRoom(RoomEntity roomEntity);

    /**
     * Finds room entity by its unique name
     * @param name unique name of Room
     * @return room entity
     */
    Optional<RoomEntity> findByName(String name);

    /**
     * Finds room entity by its unique id
     * @param id unique id of entity
     * @return room entity
     */
    Optional<RoomEntity> findById(Long id);

    /**
     * @see PageResult
     * @see PageInfo
     */
    PageResult<RoomEntity> filterRooms(RoomFilter filter, PageInfo pageInfo);

    /**
     * Returns list of rooms, which don't have any reservation in given range of dates, optionally filtered by given
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
    PageResult<RoomEntity> findAvailableRooms(Date availableFrom, Date availableTo, RoomFilter roomPropertyRestrictions,
                                        String city, PageInfo pageInfo);
}