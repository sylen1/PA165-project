package cz.muni.pa165.bookingmanager.web.controller.admin;

import cz.muni.pa165.bookingmanager.iface.dto.ReservationDto;
import cz.muni.pa165.bookingmanager.iface.facade.ReservationFacade;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.web.WebAppConstants;
import cz.muni.pa165.bookingmanager.web.forms.ReservationPtoValidator;
import cz.muni.pa165.bookingmanager.web.pto.ReservationPto;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import javax.validation.Valid;
import java.sql.Date;
import java.util.Optional;

/**
 * @author Mojmír Odehnal, 374422
 */
@Controller
@RequestMapping("/admin/reservation")
public class AdminController {

    final static Logger log = LoggerFactory.getLogger(AdminController.class);

    @Inject
    private Mapper mapper;

    @Inject
    private ReservationFacade reservationFacade;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        if (binder.getTarget() instanceof ReservationPto) {
            binder.addValidators(new ReservationPtoValidator(reservationFacade));
        }
    }

    @RequestMapping("/list")
    public String list(@RequestParam(name = "p", defaultValue = "1") Integer page, Model model){
        log.debug("admin r9nctl list");
        PageResult<ReservationDto> pageResult = reservationFacade.findAll(new PageInfo(page-1, WebAppConstants.DEFAULT_PAGE_SIZE));

        model.addAttribute("page", pageResult);

        return "/reservation/list";
    }

    @RequestMapping("/{id}")
    public String view(@PathVariable("id") Long rid, Model model){
        log.debug("admin r9nctl view");
        model.addAttribute("resOptional",reservationFacade.findById(rid).map(x-> mapper.map(x,ReservationPto.class)));
        return "reservation/view";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String editGet(@PathVariable("id") Long rid, Model model) {
        log.debug("admin r9nctl editget");
        Optional<ReservationDto> rdo = reservationFacade.findById(rid);
        if(!rdo.isPresent()) throw new IllegalArgumentException("Reservation does not exist");
        model.addAttribute("res", mapper.map(rdo.get(), ReservationPto.class));
        return "reservation/edit";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String editPost(@PathVariable("id") Long rid, @Valid @ModelAttribute("res") ReservationPto pto,
                           BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder) {
        log.debug("admin r9nctl editpost");
        if(!rid.equals(pto.getId())) throw new IllegalArgumentException("R9n ID unequal to PTO ID");
        if(!bindingResult.hasErrors()) {
            ReservationDto dto = reservationFacade.findById(pto.getId()).get();
            dto.setState(pto.getState());
            dto.setStartDate(pto.getStartDate());
            dto.setEndDate(pto.getEndDate());
            dto = reservationFacade.updateReservation(dto);
            redirectAttributes.addFlashAttribute("alert_success", "Reservation with ID" + dto.getId() + " was successfully updated");
            return "redirect:" + uriBuilder.path("/admin/reservation/{id}").buildAndExpand(dto.getId()).encode().toUriString();
        }

        return "reservation/edit";
    }
}
