package cz.muni.pa165.bookingmanager.web;

import cz.muni.pa165.bookingmanager.iface.dto.AccountState;
import cz.muni.pa165.bookingmanager.iface.dto.UserDto;
import cz.muni.pa165.bookingmanager.iface.facade.UserFacade;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.web.pto.UserRegistrationPto;
import org.apache.commons.lang3.tuple.Pair;
import org.dozer.Mapper;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.sql.Date;
import java.text.SimpleDateFormat;

@Controller
public class ExampleController {

    @Inject
    private UserFacade userFacade;
    @Inject
    private Mapper mapper;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, false));
    }

    @RequestMapping("/foo.php")
    public String foo(){
        return "foo";
    }


    @RequestMapping("/cool.php")
    public String cool(Model model){
        String title = "This is page title";
        UserDto userDto = new UserDto();
        userDto.setName("Jack Daniels");
        userDto.setEmail("jack@daniels.com");
        userDto.setAccountState(AccountState.ADMIN);
        UserDto alenka = new UserDto();
        alenka.setName("Alenka");
        alenka.setPhoneNumber("+42012345678");
        alenka.setAccountState(AccountState.CUSTOMER);
        alenka.setBirthDate(Date.valueOf("1990-1-10"));
        alenka.setAddress("alenka@email.com");

        model.addAttribute("users", userFacade.findAll(new PageInfo(0, 100)).getEntries());
        model.addAttribute("title", title);
        model.addAttribute("user", userDto);
        model.addAttribute("alenka", alenka);

        return "cool";
    }

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/login.php")
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/registration.php", method = RequestMethod.GET)
    public String registrationGet(@ModelAttribute UserRegistrationPto userRegistrationPto, Model model){
        if (userRegistrationPto == null){
            model.addAttribute("userRegistrationPto", new UserRegistrationPto());
        }

        return "registration";
    }

    @RequestMapping(value = "/registration.php", method = RequestMethod.POST)
    public String registrationPost(@ModelAttribute UserRegistrationPto userRegistrationPto, Model model){
        UserDto userDto = mapper.map(userRegistrationPto, UserDto.class);
        Pair<UserDto, String> registerResult;
        try {
            registerResult = userFacade.registerUser(userDto, userRegistrationPto.getPassword());
        } catch (Exception e) {
            return "registration";
        }

        AuthenticationProviderImpl.logInUser(userDto, userRegistrationPto.getPassword());

        model.addAttribute("email", userDto.getEmail());
        model.addAttribute("token", registerResult.getRight());

        return "registration_successful";
    }

    @RequestMapping("/confirm.php")
    public String confirmUser(@RequestParam String email, @RequestParam String token, Model model){
        boolean success = userFacade.confirmUserRegistration(email, token);
        model.addAttribute("success", success ? "success" : "failure");

        return "confirm";
    }
}
