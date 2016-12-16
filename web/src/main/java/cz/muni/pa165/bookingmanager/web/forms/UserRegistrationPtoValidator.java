package cz.muni.pa165.bookingmanager.web.forms;

import cz.muni.pa165.bookingmanager.iface.dto.UserDto;
import cz.muni.pa165.bookingmanager.iface.facade.UserFacade;
import cz.muni.pa165.bookingmanager.web.pto.UserRegistrationPto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

/**
 * @author Matej Harcar, 422714
 */
public class UserRegistrationPtoValidator implements Validator {

    private UserFacade ufacade;

    public UserRegistrationPtoValidator(UserFacade uf){
        this.ufacade = uf;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRegistrationPto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRegistrationPto urpto = (UserRegistrationPto) target;
        Optional<UserDto> oudto = ufacade.findByEmail(urpto.getEmail());
        if(oudto.isPresent()) errors.rejectValue("email","UserEmailAlreadyExists");
        oudto = ufacade.findByPhoneNumber(urpto.getPhoneNumber());
        if(oudto.isPresent()) errors.rejectValue("phoneNumber","UserPhoneAlreadyExists");
    }
}
