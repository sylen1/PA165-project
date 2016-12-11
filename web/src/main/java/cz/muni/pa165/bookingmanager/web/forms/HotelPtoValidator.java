package cz.muni.pa165.bookingmanager.web.forms;

import cz.muni.pa165.bookingmanager.iface.dto.HotelDto;
import cz.muni.pa165.bookingmanager.iface.facade.HotelFacade;
import cz.muni.pa165.bookingmanager.web.pto.HotelPto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

/**
 * @author Mojmír Odehnal, 374422
 */
public class HotelPtoValidator implements Validator {
    private HotelFacade hotelFacade;

    public HotelPtoValidator(HotelFacade hotelFacade) {
        this.hotelFacade = hotelFacade;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return HotelPto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HotelPto hotelPto = (HotelPto) target;
        Optional<HotelDto> hotelDtoOptional = hotelFacade.findByName(hotelPto.getName());
        if(hotelDtoOptional.isPresent() && !hotelDtoOptional.get().getId().equals(hotelPto.getId())) {
            errors.rejectValue("name", "HotelNameAlreadyExists");
        }
    }
}
