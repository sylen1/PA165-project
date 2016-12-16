package cz.muni.pa165.bookingmanager.web.controller.admin;

import cz.muni.pa165.bookingmanager.iface.dto.UserDto;
import cz.muni.pa165.bookingmanager.iface.facade.UserFacade;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.web.WebAppConstants;
import cz.muni.pa165.bookingmanager.web.forms.UserPtoValidator;
import cz.muni.pa165.bookingmanager.web.pto.UserPto;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Matej Harcar, 422714
 */
@Controller
@RequestMapping("/admin/user")
public class UserController {

    final static Logger log = LoggerFactory.getLogger(UserController.class);

    @Inject
    private UserFacade userFacade;

    @Inject
    private Mapper mapper;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        if (binder.getTarget() instanceof UserPto) {
            binder.addValidators(new UserPtoValidator(userFacade));
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(@RequestParam(defaultValue = "1") int pnx, Model model){
        log.debug("admin userctl list");
        PageInfo info = new PageInfo(pnx-1, WebAppConstants.DEFAULT_PAGE_SIZE);
        model.addAttribute("pageOfUsers",mapUserDtosToPtos(userFacade.findAll(info)));
        return "admin/user/list";
    }

    @RequestMapping("/{id}")
    public String view(@PathVariable("id") Long userId, Model model){
        log.debug("admin userctl view");
        model.addAttribute("userOptional",userFacade.findById(userId).map(x-> mapper.map(x,UserPto.class)));
        return "admin/user/view";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String editGet(@PathVariable("id") Long userId, Model model) {
        log.debug("admin userctl editget");
        Optional<UserDto> userDtoOptional = userFacade.findById(userId);
        if(!userDtoOptional.isPresent()) throw new IllegalArgumentException("User does not exist");
        model.addAttribute("user", mapper.map(userDtoOptional.get(), UserPto.class));
        return "admin/user/edit";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String editPost(@PathVariable("id") String userEmail, @Valid @ModelAttribute("user") UserPto pto,
                           BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder) {
        log.debug("admin userctl editpost");
        if(!userEmail.equals(pto.getEmail())) throw new IllegalArgumentException("User email unequal to email in PTO");

        if(!bindingResult.hasErrors()) {
            UserDto dto = userFacade.updateUser(mapper.map(pto, UserDto.class));

            redirectAttributes.addFlashAttribute("alert_success", "User '" + dto.getName() + "' was successfully updated");
            return "redirect:" + uriBuilder.path("/admin/user/{id}").buildAndExpand(dto.getId()).encode().toUriString();
        }

        return "admin/user/edit";
    }

    private PageResult<UserPto> mapUserDtosToPtos(PageResult<UserDto> dtoPageResult){
        List<UserPto> ptos = dtoPageResult.getEntries().stream()
                .map(x-> mapper.map(x,UserPto.class))
                .collect(Collectors.toList());
        PageResult<UserPto> rv = new PageResult<>();
        mapper.map(dtoPageResult,rv);
        rv.setEntries(ptos);
        return rv;
    }
}
