package cz.muni.pa165.bookingmanager.web.controller;

import cz.muni.pa165.bookingmanager.iface.facade.UserFacade;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

/**
 * @author Matej Harcar, 422714
 */
@Controller
@RequestMapping("/user")
public class UserController {

    final static Logger log = LoggerFactory.getLogger(cz.muni.pa165.bookingmanager.web.controller.UserController.class);

    @Inject
    private UserFacade userFacade;

    @Inject
    private Mapper mapper;

    // want user to only be able to view own account details
}
