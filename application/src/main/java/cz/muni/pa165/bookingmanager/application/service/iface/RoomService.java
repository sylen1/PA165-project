package cz.muni.pa165.bookingmanager.application.service.iface;

import cz.muni.pa165.bookingmanager.iface.util.RoomFilter;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;

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
}