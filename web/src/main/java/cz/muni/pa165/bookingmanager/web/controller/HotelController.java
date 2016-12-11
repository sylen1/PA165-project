package cz.muni.pa165.bookingmanager.web.controller;

import cz.muni.pa165.bookingmanager.iface.facade.HotelFacade;
import org.dozer.Mapper;

import javax.inject.Inject;

/**
 * Created by gasior on 10.12.2016.
 */
public class HotelController {
    @Inject
    private Mapper mapper;

    @Inject
    private HotelFacade hotelFacade;

    public String detail(){
        return "/hotel/detail";
    }
}
