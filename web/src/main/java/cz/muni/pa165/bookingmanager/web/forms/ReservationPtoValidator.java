package cz.muni.pa165.bookingmanager.web.forms;


import cz.muni.pa165.bookingmanager.iface.facade.ReservationFacade;
import cz.muni.pa165.bookingmanager.web.pto.ReservationPto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.inject.Inject;

public class ReservationPtoValidator implements Validator {
    @Inject
    private ReservationFacade reservationFacade;

    public ReservationPtoValidator(ReservationFacade reservationFacade) {
        this.reservationFacade = reservationFacade;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ReservationPto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ReservationPto reservationPto = (ReservationPto) target;
        if (reservationPto.getEndDate() != null && !reservationPto.getStartDate().before(reservationPto.getEndDate())){
            errors.rejectValue("endDate", "endDateAfterStarting");
        }
        if (reservationFacade.isRoomReserved(reservationPto.getRoomId(), reservationPto.getStartDate(), reservationPto.getEndDate())) {
            errors.rejectValue("endDate", "roomAlreadyReserved");
        }
    }
}
