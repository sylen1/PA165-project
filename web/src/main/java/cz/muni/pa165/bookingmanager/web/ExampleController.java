package cz.muni.pa165.bookingmanager.web;

import cz.muni.pa165.bookingmanager.iface.dto.AccountState;
import cz.muni.pa165.bookingmanager.iface.dto.UserDto;
import cz.muni.pa165.bookingmanager.iface.facade.UserFacade;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.web.pto.UserRegistrationPto;
import org.apache.commons.lang3.tuple.Pair;
import org.dozer.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.validation.Valid;
import java.sql.Date;

@Controller
public class ExampleController {

    @Inject
    private UserFacade userFacade;
    @Inject
    private Mapper mapper;

    @RequestMapping("/foo")
    public String foo(){
        return "foo";
    }


    @RequestMapping("/cool")
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

 /*  @RequestMapping("/")
    public String index(){

        return "index";
    }*/



}
