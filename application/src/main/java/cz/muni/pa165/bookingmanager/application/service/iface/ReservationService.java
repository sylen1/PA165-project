package cz.muni.pa165.bookingmanager.application.service.iface;

import cz.muni.pa165.bookingmanager.iface.util.HotelStatistics;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.ReservationFilter;
import cz.muni.pa165.bookingmanager.persistence.entity.ReservationEntity;

import java.util.Date;
import java.util.Optional;

/**
 * Interface for service layer for manipulation with reservations
 * @author Mojmír Odehnal, 374422
 */
public interface ReservationService extends PageableService<ReservationEntity> {
    /**
     * Creates reservation for room, customer and span of dates. All this info
     * is expected to be present in given ReservationEntity instance.
     * @param reservation reservation entity to be saved
     * @return saved reservation info
     */
    ReservationEntity createReservation(ReservationEntity reservation);
    
    /**
     * Updates information about reservation according to information present in
     * given ReservationDto instance. Id parameter present on the reservationDto
     * specifies existing reservation record, which is ment to be updated.
     * @param reservation reservation entity to be updated
     * @return saved reservation info
     */
    ReservationEntity updateReservation(ReservationEntity reservation);
    
    /**
     * Returns one page of all recorded reservations. Size of page and which
     * page of the result should be returned is specified in given PageInfo
     * instance.
     * @param pageInfo contains pagination parameters
     * @return Instance of PageResult containing part of the result
     */
    @Override
    PageResult<ReservationEntity> findAll(PageInfo pageInfo);
    
    /**
     * If reservation with given ID exists in the data source, this method
     * returns its representation in ReservationDto encapsulated in Optional.
     * Otherwise, empty Optional is returned.
     * @param id id of reservation to be searched for
     * @return Instance of Optional, either empty or with instance of ReservationEntity.
     */
    Optional<ReservationEntity> findById(Long id);
    
    /**
     * Returns one page reservations filtered by properties of given instance of
     * ReservationFilter. 
     * @param filter instance of ReservationFilter restricting the result
     * @return PageResult instance containing part of the result
     */
    PageResult<ReservationEntity> findFiltered(ReservationFilter filter, PageInfo pageInfo);

    /**
     * Tells, whether specified room is already reserved in given interval of dates
     *
     * @param roomId id of concerned room
     * @param intervalStart inclusive start date of interval for the statistics gathering
     * @param intervalEnd exclusive end date of interval for the statistics gathering
     * @return instance of HotelStatistics full of statistics
     */
    Boolean isRoomReserved(Long roomId, Date intervalStart, Date intervalEnd);

    /**
     * Assembles instance of HotelStatistics with statistics gathered from database.
     * Takes into account only completed Reservations, so only those with state PAID or ENDED.
     *
     * @param hotelId id of concerned hotel
     * @param intervalStart inclusive start date of interval for the statistics gathering
     * @param intervalEnd exculsive end date of interval for the statistics gathering
     * @return instance of HotelStatistics full of statistics
     */
    HotelStatistics gatherHotelStatistics(Long hotelId, Date intervalStart, Date intervalEnd);
}
