package cz.muni.pa165.bookingmanager.application.facade;

import cz.muni.pa165.bookingmanager.application.service.iface.RoomService;
import cz.muni.pa165.bookingmanager.application.service.iface.UserService;
import cz.muni.pa165.bookingmanager.iface.facade.RoomFacade;
import cz.muni.pa165.bookingmanager.iface.facade.UserFacade;
import org.dozer.Mapper;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;

/**
 * @author Gasior
 */
public class RoomFacadeTest {
    @Inject
    private ApplicationContext context;

    @Inject
    private Mapper mapper;

    private RoomFacade roomFacade;

    @Mock
    private RoomService roomService;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        roomFacade = new RoomFacadeImpl(roomService, context.getBean(Mapper.class));
    }
}
