package cz.muni.pa165.bookingmanager.web.controller;

import cz.muni.pa165.bookingmanager.iface.dto.RoomDto;
import cz.muni.pa165.bookingmanager.iface.dto.UserDto;
import cz.muni.pa165.bookingmanager.iface.facade.HotelFacade;
import cz.muni.pa165.bookingmanager.iface.facade.ReservationFacade;
import cz.muni.pa165.bookingmanager.iface.facade.RoomFacade;
import cz.muni.pa165.bookingmanager.iface.facade.UserFacade;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.RoomFilter;
import cz.muni.pa165.bookingmanager.web.AuthenticationProviderImpl;
import cz.muni.pa165.bookingmanager.web.WebAppConstants;
import cz.muni.pa165.bookingmanager.web.forms.UserRegistrationPtoValidator;
import cz.muni.pa165.bookingmanager.web.pto.*;
import org.apache.commons.lang3.tuple.Pair;
import org.dozer.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gasior on 10.12.2016.
 */
@Controller
@RequestMapping("")
public class HomeController {

    private RoomFilterPto filterCache;

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

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        if (binder.getTarget() instanceof UserRegistrationPto) {
            binder.addValidators(new UserRegistrationPtoValidator(userFacade));
        }
    }

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/list")
    public String view(Model model, @RequestParam(name = "p", defaultValue = "1") Integer page) {

        PageResult<RoomDto> rooms = null;
        if(filterCache == null) {
            rooms =  roomFacade.findAll(new PageInfo(page-1, WebAppConstants.DEFAULT_PAGE_SIZE));
        } else
        {
          rooms =  roomFacade.findAvailableRooms(filterCache.getDateFrom(), filterCache.getDateTo(), new RoomFilter(filterCache.getBedFrom(),filterCache.getBedTo(), filterCache.getPriceFrom(),
                  filterCache.getPriceTo()),filterCache.getCity(), new PageInfo(page-1, WebAppConstants.DEFAULT_PAGE_SIZE));

        }


        PageResult<RoomPto> roomPtosPage = new PageResult<>();
        List<RoomPto> roomPtos = new ArrayList<>();

        for(Object r : rooms.getEntries().stream().map(x -> mapper.map(x, RoomPto.class)).toArray()) {
            roomPtos.add((RoomPto) r);
        }

        roomPtosPage.setEntries(roomPtos);
        roomPtosPage.setPageSize(rooms.getPageSize());
        roomPtosPage.setPageNumber(rooms.getPageNumber());
        roomPtosPage.setPageCount(rooms.getPageCount());
        roomPtosPage.setTotalEntries(rooms.getTotalEntries());

        model.addAttribute("filter", new RoomFilterPto());
        model.addAttribute("rooms", roomPtosPage);

        return "list";
    }

    @RequestMapping("/clear")
    public String clear() {

        filterCache = null;

        return "redirect:/list";
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public String listPost(@Valid @ModelAttribute("filter") RoomFilterPto filter, @RequestParam(name = "p", defaultValue = "1") Integer page, BindingResult bindingResult,
                          Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder) {

        if(filterCache == null) {
            filterCache = filter;
        }

        PageResult<RoomDto> rooms =  roomFacade.findAvailableRooms(filter.getDateFrom(), filter.getDateTo(), new RoomFilter(filter.getBedFrom(),filter.getBedTo(), filter.getPriceFrom(),filter.getPriceTo()),filter.getCity(), new PageInfo(page-1, WebAppConstants.DEFAULT_PAGE_SIZE));
        List<RoomPto> roomPtos = new ArrayList<>();

        for(Object r : rooms.getEntries().stream().map(x -> mapper.map(x, RoomPto.class)).toArray()) {
            roomPtos.add((RoomPto) r);
        }
        PageResult<RoomPto> roomPtosPage = new PageResult<>();
        roomPtosPage.setEntries(roomPtos);
        roomPtosPage.setPageSize(rooms.getPageSize());
        roomPtosPage.setPageNumber(rooms.getPageNumber());
        roomPtosPage.setPageCount(rooms.getPageCount());
        roomPtosPage.setTotalEntries(rooms.getTotalEntries());

        model.addAttribute("filter", filter);
        model.addAttribute("rooms", roomPtosPage);

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
    public String profile(Model model) {
        String email = AuthenticationProviderImpl.getLoggedUserEmail();
        model.addAttribute("userOptional",userFacade.findByEmail(email).map(x-> mapper.map(x,UserPto.class)));
        return "/profile";
    }

}
