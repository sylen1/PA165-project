package cz.muni.pa165.bookingmanager.iface.facade;

import cz.muni.pa165.bookingmanager.iface.dto.ReservationDto;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.ReservationFilter;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;

import java.util.Optional;

/**
 * Interface for facade layer for manipulation with reservations
 * @author Mojmír Odehnal, 374422
 */
public interface ReservationFacade extends PageableFacade<ReservationDto> {
    /**
     * Creates reservation for room, customer and span of dates. All this info
     * is expected to be present in given ReservationDto instance.
     * @param reservationDto reservation info to be saved
     * @return saved reservation info
     */
    ReservationDto createReservation(ReservationDto reservationDto);
    
    /**
     * Updates information about reservation according to information present in
     * given ReservationDto instance. Id parameter present on the reservationDto
     * specifies existing reservation record, which is meant to be updated.
     * @param reservationDto reservation info to be updated
     * @return saved reservation info
     */
    ReservationDto updateReservation(ReservationDto reservationDto);
    
    /**
     * Returns one page of all recorded reservations. Size of page and which
     * page of the result should be returned is specified in given PageInfo
     * instance.
     * @param pageInfo contains pagination parameters
     * @return Instance of PageResult containing part of the result
     */
    @Override
    PageResult<ReservationDto> findAll(PageInfo pageInfo);
    
    /**
     * If reservation with given ID exists in the data source, this method
     * returns its representation in ReservationDto encapsulated in Optional.
     * Otherwise, empty Optional is returned.
     * @param id id of reservation to be searched for
     * @return Instance of Optional, either empty or with instance of ReservationDto.
     */
    Optional<ReservationDto> findById(Long id);
    
    /**
     * Returns one page reservations filtered by properties of given instance of
     * ReservationFilter. 
     * @param filter instance of ReservationFilter restricting the result
     * @param pageInfo contains pagination parameters
     * @return PageResult instance containing part of the result
     */
    PageResult<ReservationDto> findFiltered(ReservationFilter filter, PageInfo pageInfo);

}
