package cz.muni.pa165.bookingmanager.web.controller;

import cz.muni.pa165.bookingmanager.iface.dto.ReservationDto;
import cz.muni.pa165.bookingmanager.iface.facade.ReservationFacade;
import cz.muni.pa165.bookingmanager.iface.facade.RoomFacade;
import cz.muni.pa165.bookingmanager.iface.facade.UserFacade;
import cz.muni.pa165.bookingmanager.web.AuthenticationProviderImpl;
import cz.muni.pa165.bookingmanager.web.forms.ReservationPtoValidator;
import cz.muni.pa165.bookingmanager.web.pto.ReservationPto;
import org.dozer.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/user/reservation")
public class ReservationController {

    @Inject
    private ReservationFacade reservationFacade;

    @Inject
    private RoomFacade roomFacade;

    @Inject
    private UserFacade userFacade;

    @Inject
    private Mapper mapper;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        if (binder.getTarget() instanceof ReservationPto) {
            binder.addValidators(new ReservationPtoValidator(reservationFacade));
        }
    }


    @RequestMapping("/form")
    public String reservationForm(@RequestParam long room, Model model){
        ReservationPto reservationPto = new ReservationPto();
        reservationPto.setRoomId(room);

        model.addAttribute("reservationPto", reservationPto);

        return "/reservation/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String reservationFormPost(@ModelAttribute @Valid ReservationPto reservationPto,
                                      BindingResult result,
                                      HttpServletRequest request){
        if (!result.hasErrors()){
            createReservation(reservationPto);
            return "redirect:/user/reservation/success";
        }

        request.setAttribute("room", reservationPto.getRoomId());
        return "reservation/form";
    }

    private void createReservation(ReservationPto reservationPto) {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setStartDate(reservationPto.getStartDate());
        reservationDto.setEndDate(reservationPto.getEndDate());
        reservationDto.setRoom(roomFacade.findById(reservationPto.getRoomId()).orElseThrow(IllegalArgumentException::new));
        reservationDto.setCustomer(userFacade.findByEmail(AuthenticationProviderImpl.getLoggedUserEmail()).orElseThrow(IllegalArgumentException::new));

        reservationFacade.createReservation(reservationDto);
    }

    @RequestMapping("/success")
    public String reservationSuccessful(){
        return "/reservation/success";
    }


}
