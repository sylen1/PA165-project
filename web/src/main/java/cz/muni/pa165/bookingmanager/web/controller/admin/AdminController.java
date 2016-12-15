package cz.muni.pa165.bookingmanager.web.controller.admin;

import cz.muni.pa165.bookingmanager.iface.dto.ReservationDto;
import cz.muni.pa165.bookingmanager.iface.facade.ReservationFacade;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.web.WebAppConstants;
import org.dozer.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;

/**
 * @author Mojmír Odehnal, 374422
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Inject
    private Mapper mapper;

    @Inject
    private ReservationFacade reservationFacade;

    @RequestMapping("/reservation/list")
    public String list(@RequestParam(name = "p", defaultValue = "1") Integer page, Model model){
        PageResult<ReservationDto> pageResult = reservationFacade.findAll(new PageInfo(page-1, WebAppConstants.DEFAULT_PAGE_SIZE));

        model.addAttribute("page", pageResult);

        return "/reservation/list";
    }
}
