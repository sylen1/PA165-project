package cz.muni.pa165.bookingmanager.web;

import cz.muni.pa165.bookingmanager.iface.dto.AccountState;
import cz.muni.pa165.bookingmanager.iface.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

@Controller
public class ExampleController {
    @RequestMapping("/foo.php")
    public String foo(){
        return "foo";
    }


    @RequestMapping("/cool.php")
    public String cool(Model model){
        List<String> numbers = Arrays.asList("one", "two", "three");
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

        model.addAttribute("numbers", numbers);
        model.addAttribute("title", title);
        model.addAttribute("user", userDto);
        model.addAttribute("alenka", alenka);

        return "cool";
    }

    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
