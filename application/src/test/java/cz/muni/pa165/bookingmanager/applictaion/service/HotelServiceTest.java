package cz.muni.pa165.bookingmanager.applictaion.service;

import cz.muni.pa165.bookingmanager.application.service.iface.HotelService;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import org.junit.Test;

@ContextConfiguration(locations = { "/application-context.xml" } )
@RunWith(SpringJUnit4ClassRunner.class)
public class HotelServiceTest {
    @Inject
    private HotelService hotelService;

    @Test
    public void hello() {
    }

}
