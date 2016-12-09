package cz.muni.pa165.bookingmanager.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Mojmír Odehnal, 374422
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("")
    public String dashboard() {
        return "foo";
    }
}
