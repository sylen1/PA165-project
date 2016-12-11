package cz.muni.pa165.bookingmanager.web.controller.admin;

import cz.muni.pa165.bookingmanager.iface.dto.HotelDto;
import cz.muni.pa165.bookingmanager.iface.facade.HotelFacade;
import cz.muni.pa165.bookingmanager.iface.facade.ReservationFacade;
import cz.muni.pa165.bookingmanager.iface.facade.RoomFacade;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.web.WebAppConstants;
import cz.muni.pa165.bookingmanager.web.forms.HotelPtoValidator;
import cz.muni.pa165.bookingmanager.web.pto.HotelPto;
import org.dozer.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Mojmír Odehnal, 374422
 */
@Controller
@RequestMapping("/admin/hotel")
public class HotelController {
    @Inject
    private HotelFacade hotelFacade;
    @Inject
    private ReservationFacade reservationFacade;

    @Inject
    private Mapper mapper;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        if (binder.getTarget() instanceof HotelPto) {
            binder.addValidators(new HotelPtoValidator(hotelFacade));
        }
    }

    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int p, Model model) {
        model.addAttribute("pageOfHotels", mapToPagePtos(hotelFacade.findAll(new PageInfo(p-1, WebAppConstants.defaultPageSize))));
        return "admin/hotel/list";
    }

    @RequestMapping("/{id}")
    public String view(@PathVariable("id") Long hotelId, Model model) {
        model.addAttribute("hotelOptional", hotelFacade.findById(hotelId).map(x -> mapper.map(x, HotelPto.class)));
        model.addAttribute("requestedId", hotelId);
        return "admin/hotel/view";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addGet(Model model) {
        model.addAttribute("hotel", new HotelPto());
        return "admin/hotel/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addPost(@Valid @ModelAttribute("hotel") HotelPto pto, BindingResult bindingResult,
                          Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder) {
        if (!bindingResult.hasErrors()) {
            HotelDto dto = hotelFacade.registerHotel(mapper.map(pto, HotelDto.class));

            redirectAttributes.addFlashAttribute("alert_success", "Hotel '" + dto.getName() + "' was created");
            return "redirect:" + uriBuilder.path("/admin/hotel/{id}").buildAndExpand(dto.getId()).encode().toUriString();
        }

        return "admin/hotel/add";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String editGet(@PathVariable("id") Long hotelId, Model model) {
        Optional<HotelDto> hotelDtoOptional = hotelFacade.findById(hotelId);
        if(!hotelDtoOptional.isPresent()) throw new IllegalArgumentException("Given ID is invalid");
        model.addAttribute("hotel", mapper.map(hotelDtoOptional.get(), HotelPto.class));
        return "admin/hotel/edit";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String editPost(@PathVariable("id") Long hotelId, @Valid @ModelAttribute("hotel") HotelPto pto,
            BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder) {
        if(!hotelId.equals(pto.getId())) throw new IllegalArgumentException("Weird");

        if(!bindingResult.hasErrors()) {
            HotelDto dto = hotelFacade.updateHotelInformation(mapper.map(pto, HotelDto.class));

            redirectAttributes.addFlashAttribute("alert_success", "Hotel '" + dto.getName() + "' was updated");
            return "redirect:" + uriBuilder.path("/admin/hotel/{id}").buildAndExpand(dto.getId()).encode().toUriString();
        }

        return "admin/hotel/edit";
    }




    private PageResult<HotelPto> mapToPagePtos(PageResult<HotelDto> dtoPageResult){
        List<HotelPto> ptos = dtoPageResult.getEntries()
                .stream()
                .map(x -> mapper.map(x, HotelPto.class))
                .collect(Collectors.toList());

        PageResult<HotelPto> result = new PageResult<>();
        mapper.map(dtoPageResult, result);
        result.setEntries(ptos);

        return result;
    }
}
