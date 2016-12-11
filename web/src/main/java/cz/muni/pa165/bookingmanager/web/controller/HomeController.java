package cz.muni.pa165.bookingmanager.web.controller;

import cz.muni.pa165.bookingmanager.iface.facade.HotelFacade;
import cz.muni.pa165.bookingmanager.iface.facade.ReservationFacade;
import cz.muni.pa165.bookingmanager.iface.facade.RoomFacade;
import cz.muni.pa165.bookingmanager.iface.facade.UserFacade;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

/**
 * Created by gasior on 10.12.2016.
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @Inject
    private HotelFacade hotelFacade;

    @Inject
    private RoomFacade roomFacade;

    @Inject
    private ReservationFacade reservationFacade;

    @Inject
    private UserFacade userFacade;

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/list.fortran")
    public String list(){
        return "list";
    }
}
