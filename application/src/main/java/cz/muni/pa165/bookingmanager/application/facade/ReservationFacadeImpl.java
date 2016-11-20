package cz.muni.pa165.bookingmanager.application.facade;

import cz.muni.pa165.bookingmanager.application.service.iface.ReservationService;
import cz.muni.pa165.bookingmanager.iface.dto.ReservationDto;
import cz.muni.pa165.bookingmanager.iface.util.ReservationFilter;
import cz.muni.pa165.bookingmanager.iface.facade.ReservationFacade;
import cz.muni.pa165.bookingmanager.iface.util.Page;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import javax.inject.Inject;
import java.util.Optional;
import org.apache.commons.lang3.Validate;

/**
 * Implemetation of ReservationFacade interface
 * @author Mojmír Odehnal, 374422
 */
public class ReservationFacadeImpl implements ReservationFacade {
    @Inject
    private ReservationService reservationService;
    
    @Override
    public ReservationDto createReservation(ReservationDto reservationDto) {
//        Validate.isTrue(reservationDto.getId() == null);
//
//        ReservationEntity entity = convert(reservationDto);
//        ReservationEntity saved = reservationService.registerHotel(entity);
//
//        return convert(saved);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ReservationDto updateReservation(ReservationDto reservationDto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Page<ReservationDto> findAll(PageInfo pageInfo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Optional<ReservationDto> findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Page<ReservationDto> findFiltered(ReservationFilter filter) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
