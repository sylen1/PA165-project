package cz.muni.pa165.bookingmanager.web.controller.admin;

import cz.muni.pa165.bookingmanager.iface.dto.HotelDto;
import cz.muni.pa165.bookingmanager.iface.dto.RoomDto;
import cz.muni.pa165.bookingmanager.iface.facade.HotelFacade;
import cz.muni.pa165.bookingmanager.iface.facade.RoomFacade;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.web.WebAppConstants;
import cz.muni.pa165.bookingmanager.web.forms.HotelPtoValidator;
import cz.muni.pa165.bookingmanager.web.pto.HotelPto;
import cz.muni.pa165.bookingmanager.web.pto.RoomPto;
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
import java.math.BigDecimal;
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
    private RoomFacade roomFacade;

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
        model.addAttribute("pageOfHotels", mapToPagePtos(hotelFacade.findAll(new PageInfo(p-1, WebAppConstants.DEFAULT_PAGE_SIZE))));
        return "admin/hotel/list";
    }

    @RequestMapping("/{hotelId}")
    public String view(@PathVariable("hotelId") Long hotelId, Model model) {
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

    @RequestMapping(value = "/{hotelId}/edit", method = RequestMethod.GET)
    public String editGet(@PathVariable("hotelId") Long hotelId, Model model) {
        Optional<HotelDto> hotelDtoOptional = hotelFacade.findById(hotelId);
        if(!hotelDtoOptional.isPresent()) throw new IllegalArgumentException("Given ID of hotel is invalid");
        model.addAttribute("hotel", mapper.map(hotelDtoOptional.get(), HotelPto.class));

        return "admin/hotel/edit";
    }

    @RequestMapping(value = "/{hotelId}/edit", method = RequestMethod.POST)
    public String editPost(@PathVariable("hotelId") Long hotelId, @Valid @ModelAttribute("hotel") HotelPto pto,
                           BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder) {
        if(!hotelId.equals(pto.getId()))
            throw new IllegalArgumentException("ID of hotel in URL is different than ID received from the form");

        Optional<HotelDto> storedDtoOptional = hotelFacade.findById(hotelId);
        if(!storedDtoOptional.isPresent()) throw new IllegalArgumentException("Hotel with received ID has not been found");

        if(!bindingResult.hasErrors()) {
            HotelDto dto = mapper.map(pto, HotelDto.class);
            dto.setRooms(storedDtoOptional.get().getRooms());
            hotelFacade.updateHotelInformation(dto);
            redirectAttributes.addFlashAttribute("alert_success", "Hotel '" + dto.getName() + "' was updated");
            return "redirect:" + uriBuilder.path("/admin/hotel/{id}").buildAndExpand(dto.getId()).encode().toUriString();
        }

        return "admin/hotel/edit";
    }

    @RequestMapping(value = "/{hotelId}/room/add", method = RequestMethod.GET)
    public String addRoomGet(@PathVariable("hotelId") Long hotelId, Model model) {
        Optional<HotelDto> hotelDtoOptional = hotelFacade.findById(hotelId);
        if(!hotelDtoOptional.isPresent()) throw new IllegalArgumentException("Given ID of hotel is invalid");
        model.addAttribute("hotel", mapper.map(hotelDtoOptional.get(), HotelPto.class));
        RoomPto pto = new RoomPto();
        pto.setPrice(BigDecimal.ZERO);
        model.addAttribute("room", pto);
        return "admin/room/add";
    }

    @RequestMapping(value = "/{hotelId}/room/add", method = RequestMethod.POST)
    public String addRoomPost(@PathVariable("hotelId") Long hotelId, @Valid @ModelAttribute("room") RoomPto roomPto,
            BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder) {
        Optional<HotelDto> hotelDtoOptional = hotelFacade.findById(hotelId);
        if(!hotelDtoOptional.isPresent()) throw new IllegalArgumentException("Hotel with received ID has not been found");
        HotelDto hotelDto = hotelDtoOptional.get();

        if (!bindingResult.hasErrors()) {
            RoomDto roomDto = roomFacade.registerRoom(mapper.map(roomPto, RoomDto.class));
            hotelDto.addRoom(roomDto);
            hotelFacade.updateHotelInformation(hotelDto);

            redirectAttributes.addFlashAttribute("alert_success", "Room '" + roomDto.getName()
                    + "' was successfully created in hotel '" + hotelDto.getName() + "'");
            return "redirect:" + uriBuilder.path("/admin/hotel/{id}").buildAndExpand(hotelDto.getId()).encode().toUriString();
        }

        model.addAttribute("hotel", mapper.map(hotelDto, HotelPto.class));

        return "admin/room/add";
    }

    @RequestMapping(value = "/{hotelId}/room/{roomId}/edit", method = RequestMethod.GET)
    public String editRoomGet(@PathVariable("hotelId") Long hotelId, @PathVariable("roomId") Long roomId, Model model) {
        Optional<RoomDto> roomDtoOptional = roomFacade.findById(roomId);
        if(!roomDtoOptional.isPresent()) throw new IllegalArgumentException("Given ID of room is invalid");

        if(!roomDtoOptional.map(RoomDto :: getHotelId).equals(Optional.of(hotelId)))
            throw new IllegalArgumentException("ID of hotel in URL is different than ID stored in the room");

        Optional<HotelDto> hotelDtoOptional = hotelFacade.findById(hotelId);
        if(!hotelDtoOptional.isPresent()) throw new IllegalArgumentException("Given ID of hotel is invalid");

        model.addAttribute("hotel", mapper.map(hotelDtoOptional.get(), HotelPto.class));
        model.addAttribute("room", mapper.map(roomDtoOptional.get(), RoomPto.class));
        return "admin/room/edit";
    }

    @RequestMapping(value = "/{idOfHotel}/room/{roomId}/edit", method = RequestMethod.POST)
    public String editRoomPost(@PathVariable("idOfHotel") Long hotelId, @PathVariable("roomId") Long roomId,
                               @Valid @ModelAttribute("room") RoomPto roomPto, BindingResult bindingResult, Model model,
                               RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder) {
        if(!roomId.equals(roomPto.getId()))
            throw new IllegalArgumentException("ID of room in URL is different than ID received from the form");
        if(!hotelId.equals(roomPto.getHotelId()))
            throw new IllegalArgumentException("ID of hotel in URL is different than ID received from the form");

        Optional<HotelDto> hotelDtoOptional = hotelFacade.findById(hotelId);
        if(!hotelDtoOptional.isPresent()) throw new IllegalArgumentException("Given ID of hotel is invalid");
        HotelDto hotelDto = hotelDtoOptional.get();

        if(!bindingResult.hasErrors()) {
            RoomDto roomDto = roomFacade.updateRoom(mapper.map(roomPto, RoomDto.class));

            redirectAttributes.addFlashAttribute("alert_success", "Room '" + roomDto.getName()
                    + "' in hotel '" + hotelDto.getName() + "' was updated");
            return "redirect:" + uriBuilder.path("/admin/hotel/{id}").buildAndExpand(hotelDto.getId()).encode().toUriString();
        }

        model.addAttribute("hotel", mapper.map(hotelDto, HotelPto.class));
        return "admin/room/edit";
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
