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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    public String view(@ModelAttribute("filter") RoomFilterPto filter, Model model, @RequestParam(name = "p", defaultValue = "1") Integer page) {
        if (filter == null){
            filter = new RoomFilterPto();
        }

        PageResult<RoomDto> availableRooms = roomFacade.findAvailableRooms(filter.getDateFrom(),
                filter.getDateTo(),
                new RoomFilter(filter.getBedFrom(), filter.getBedTo(), filter.getPriceFrom(), filter.getPriceTo()),
                filter.getCity(),
                new PageInfo(page-1, WebAppConstants.DEFAULT_PAGE_SIZE));

        PageResult<RoomPto> roomPtosPage = new PageResult<>();
        List<RoomPto> roomPtos = availableRooms.getEntries()
                .stream()
                .map(x -> mapper.map(x, RoomPto.class))
                .collect(Collectors.toList());

        roomPtosPage.setEntries(roomPtos);
        roomPtosPage.setPageSize(availableRooms.getPageSize());
        roomPtosPage.setPageNumber(availableRooms.getPageNumber());
        roomPtosPage.setPageCount(availableRooms.getPageCount());
        roomPtosPage.setTotalEntries(availableRooms.getTotalEntries());

        model.addAttribute("filter", filter);
        model.addAttribute("rooms", roomPtosPage);

        return "list";
    }

    @RequestMapping("/clear")
    public String clear() {

        return "redirect:/list";
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public String listPost(@Valid @ModelAttribute("filter") RoomFilterPto filter, BindingResult bindingResult, @RequestParam(name = "p", defaultValue = "1") Integer page,
                         RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder) {

        redirectAttributes.addFlashAttribute("filter", filter);

        return "redirect:list";
    }

    @RequestMapping("/detail/{hotel_id}")
    public String detail(@PathVariable("hotel_id") Long hotelId, @RequestParam(name = "from", defaultValue = "0") Long from,  @RequestParam(name = "to", defaultValue = "0") Long to, Model model){

        Date fromDate = new Date();
        Date toDate = new Date();

        if(from == 0) {
            fromDate.setTime(toDate.getTime()-365*24*60*60*1000);
        } else  {
            fromDate.setTime(from);
        }
        if(to == 0) {
            toDate.setTime(toDate.getTime()+365*24*60*60*1000);
        } else  {
            toDate.setTime(to);
        }
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        HotelPto hotelPto = mapper.map(hotelFacade.findById(hotelId).get(), HotelPto.class);
        hotelPto.setHotelStatistics(reservationFacade.gatherHotelStatistics(hotelPto.getId(), fromDate, toDate));
        model.addAttribute("from", df.format(fromDate));
        model.addAttribute("to",  df.format(toDate));
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
