package cz.muni.pa165.bookingmanager.web.controller;

import cz.muni.pa165.bookingmanager.iface.dto.RoomDto;
import cz.muni.pa165.bookingmanager.iface.dto.UserDto;
import cz.muni.pa165.bookingmanager.iface.facade.HotelFacade;
import cz.muni.pa165.bookingmanager.iface.facade.ReservationFacade;
import cz.muni.pa165.bookingmanager.iface.facade.RoomFacade;
import cz.muni.pa165.bookingmanager.iface.facade.UserFacade;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.RoomFilter;
import cz.muni.pa165.bookingmanager.web.AuthenticationProviderImpl;
import cz.muni.pa165.bookingmanager.web.pto.HotelPto;
import cz.muni.pa165.bookingmanager.web.pto.RoomPto;
import cz.muni.pa165.bookingmanager.web.pto.UserRegistrationPto;
import org.apache.commons.lang3.tuple.Pair;
import org.dozer.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gasior on 10.12.2016.
 */
@Controller
@RequestMapping("")
public class HomeController {
    @Inject
    private UserFacade userFacade;
    @Inject
    private RoomFacade roomFacade;
    @Inject
    private HotelFacade hotelFacade;
    @Inject
    private ReservationFacade reservationFacade;
    @Inject
    private Mapper mapper;

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/list.fortran")
    public String view(Model model) {
        // TODO FILTER
        Date from = new Date();
        Date to = new Date();
        to.setTime(to.getTime()+10*24*60*60*1000);
        List<RoomDto> rooms =  roomFacade.findAvailableRooms(from, to, new RoomFilter(0,10, new BigDecimal(0), new BigDecimal(10000)), "New York", new PageInfo(0,Integer.MAX_VALUE)).getEntries();
        model.addAttribute("rooms", rooms.stream().map(x -> mapper.map(x, RoomPto.class)).toArray());
        return "list";
    }

    @RequestMapping("/detail/{hotel_id}")
    public String detail(@PathVariable("hotel_id") Long hotelId, Model model){
        // TODO FILTER FROM - TO DATE

        Date from = new Date();
        from.setTime(0);
        Date to = new Date();
        to.setTime(to.getTime()*2);
        HotelPto hotelPto = mapper.map(hotelFacade.findById(hotelId).get(), HotelPto.class);
        hotelPto.setHotelStatistics(reservationFacade.gatherHotelStatistics(hotelPto.getId(), from, to));
        model.addAttribute("hotel", hotelPto);
        return "detail";
    }
    @RequestMapping("/login")
    public String login(@RequestParam(required = false, defaultValue = "false") Boolean logout, Model model) {
        model.addAttribute("logout", logout);
        return "login";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        AuthenticationProviderImpl.logout(request);
        return "redirect:/login?logout=true";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registrationGet(Model model) {
        if (!model.containsAttribute("userRegistrationPto")) {
            model.addAttribute("userRegistrationPto", new UserRegistrationPto());
        }
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registrationPost(@Valid UserRegistrationPto userRegistrationPto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

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

    @RequestMapping("/confirm")
    public String confirmUser(@RequestParam String email, @RequestParam String token, Model model) {
        boolean success = userFacade.confirmUserRegistration(email, token);
        model.addAttribute("success", success ? "success" : "failure");

        return "confirm";
    }

    @RequestMapping("/about")
    public String about() {
        return "about";
    }

    @RequestMapping("/contact")
    public String contact() {
        return "contact";
    }

    @RequestMapping("/user/profile")
    public String profile() {
        return "profile";
    }
}