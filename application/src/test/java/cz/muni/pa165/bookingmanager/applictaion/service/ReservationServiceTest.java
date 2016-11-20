package cz.muni.pa165.bookingmanager.applictaion.service;

import cz.muni.pa165.bookingmanager.application.service.iface.ReservationService;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import javax.inject.Inject;

import cz.muni.pa165.bookingmanager.application.facade.ReservationFacadeImpl;

@ContextConfiguration(locations = { "/application-context.xml" } )
@RunWith(SpringJUnit4ClassRunner.class)
public class ReservationServiceTest {
//    @Inject
//    private ReservationService reservationService;
//    
//    @Inject
//    private ReservationFacadeImpl facade;
    
    @Test
    public void hello() {
    }
}
