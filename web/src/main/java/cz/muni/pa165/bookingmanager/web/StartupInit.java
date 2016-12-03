package cz.muni.pa165.bookingmanager.web;

import cz.muni.pa165.bookingmanager.iface.dto.AccountState;
import cz.muni.pa165.bookingmanager.iface.dto.UserDto;
import cz.muni.pa165.bookingmanager.iface.facade.UserFacade;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.sql.Date;

public class StartupInit implements ApplicationContextAware {
    private static final Logger LOG = LoggerFactory.getLogger(StartupInit.class);

    private ApplicationContext context;
    
    public void onStartUp() {
        LOG.info("Executing onStartUp");

        UserDto adminDto = new UserDto();
        adminDto.setAddress("");
        adminDto.setBirthDate(Date.valueOf("1990-1-1"));
        adminDto.setName("Admin");
        adminDto.setEmail("admin@admin.com");
        adminDto.setPhoneNumber("123");

        UserFacade userFacade = context.getBean(UserFacade.class);
        Pair<UserDto, String> adminToken = userFacade.registerUser(adminDto, "password");
        userFacade.confirmUserRegistration(adminDto.getEmail(), adminToken.getRight());

        adminDto = adminToken.getLeft();
        adminDto.setAccountState(AccountState.ADMIN);
        userFacade.updateUser(adminDto);

        System.out.println(userFacade.findByEmail(adminDto.getEmail()));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
