package cz.muni.pa165.bookingmanager.web.forms;

import cz.muni.pa165.bookingmanager.iface.dto.UserDto;
import cz.muni.pa165.bookingmanager.iface.facade.UserFacade;
import cz.muni.pa165.bookingmanager.web.pto.UserPto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

/**
 * @author Matej Harcar, 422714
 */
public class UserPtoValidator implements Validator {

    private UserFacade ufacade;

    public UserPtoValidator(UserFacade uf){
        this.ufacade = uf;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserPto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserPto upto = (UserPto) target;
        Optional<UserDto> oudto = ufacade.findByEmail(upto.getEmail());
        if(oudto.isPresent() && !oudto.get().getId().equals(upto.getId())){
            errors.rejectValue("email","UserEmailAlreadyExists");
        }
        oudto = ufacade.findByPhoneNumber(upto.getPhoneNumber());
        if(oudto.isPresent() && !oudto.get().getId().equals(upto.getId())){
            errors.rejectValue("phonenumber","UserPhoneAlreadyExists");
        }
    }
}
