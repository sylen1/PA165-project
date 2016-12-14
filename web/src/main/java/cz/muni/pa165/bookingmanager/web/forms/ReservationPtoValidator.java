package cz.muni.pa165.bookingmanager.web.forms;


import cz.muni.pa165.bookingmanager.web.pto.ReservationPto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ReservationPtoValidator implements Validator {
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
    }
}
