package cz.muni.pa165.bookingmanager.iface.facade;

import cz.muni.pa165.bookingmanager.iface.dto.ReservationDto;
import cz.muni.pa165.bookingmanager.iface.util.ReservationFilter;
import cz.muni.pa165.bookingmanager.iface.util.Page;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import java.util.Date;
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
    public ReservationDto createReservation(ReservationDto reservationDto);
    
    /**
     * Updates information about reservation according to information present in
     * given ReservationDto instance. Id parameter present on the reservationDto
     * specifies existing reservation record, which is ment to be updated.
     * @param reservationDto reservation info to be updated
     * @return saved reservation info
     */
    public ReservationDto updateReservation(ReservationDto reservationDto);
    
    /**
     * Returns one page of all recorded reservations. Size of page and which
     * page of the result should be returned is specified in given PageInfo
     * instance.
     * @param pageInfo contains pagination parameters
     * @return Instance of Page containing part of the result
     */
    @Override
    public Page<ReservationDto> findAll(PageInfo pageInfo);
    
    /**
     * If reservation with given ID exists in the data source, this method
     * returns its representation in ReservationDto encapsulated in Optional.
     * Otherwise, empty Optional is returned.
     * @param id id of reservation to be searched for
     * @return Instance of Optional, either empty or with instance of ReservationDto.
     */
    public Optional<ReservationDto> findById(Long id);
    
    /**
     * Returns one page reservations filtered by properties of given instance of
     * ReservationFilter. 
     * @param filter instance of ReservationFilter restricting the result
     * @return Page instance containing part of the result
     */
    public Page<ReservationDto> findFiltered(ReservationFilter filter);

}
