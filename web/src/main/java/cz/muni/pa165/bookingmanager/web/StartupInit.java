package cz.muni.pa165.bookingmanager.web;

import cz.muni.pa165.bookingmanager.iface.dto.AccountState;
import cz.muni.pa165.bookingmanager.iface.dto.HotelDto;
import cz.muni.pa165.bookingmanager.iface.dto.RoomDto;
import cz.muni.pa165.bookingmanager.iface.dto.UserDto;
import cz.muni.pa165.bookingmanager.iface.facade.HotelFacade;
import cz.muni.pa165.bookingmanager.iface.facade.RoomFacade;
import cz.muni.pa165.bookingmanager.iface.facade.UserFacade;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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


        seedRoomsAndHotels();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }


    private void seedRoomsAndHotels() {

        HotelFacade hotelFacade = context.getBean(HotelFacade.class);
        RoomFacade roomFacade = context.getBean(RoomFacade.class);

        List<HotelDto> hotels = new ArrayList<>();

        hotels.add(createHotel("Plaza", "New York"));
        hotels.add(createHotel("Hilton", "Los Angeles"));
        hotels.add(createHotel("Paris", "Praguq"));
        hotels.add(createHotel("Los Pollos Hermanos", "Seatle"));
        hotels.add(createHotel("Continental", "Dubai"));
        hotels.add(createHotel("Trump", "New York"));

        for (HotelDto hotel : hotels) {
            HotelDto h = hotelFacade.registerHotel(hotel);
            for (int i = 1; i < 10; i++) {
                roomFacade.registerRoom(createRoom(String.valueOf(i), h));
            }
        }


    }
    private RoomDto createRoom(String key, HotelDto hotel){

        RoomDto roomDto = new RoomDto();
        roomDto.setName("A222 " + key + " in " + hotel.getName());
        roomDto.setBedCount(Integer.valueOf(key)%5);
        roomDto.setDescription(roomDto.getName() + " :\n" +
                "Queen-size or twin beds available\n" +
                "Complimentary Wi Fi\n" +
                "Connecting room available\n" +
                "Bottega Veneta toiletries\n" +
                "Maximum occupancy 2 people\n" +
                "Nespresso coffee machine\n" +
                "30-38m²/ 323-409ft²");
        roomDto.setPrice(new BigDecimal(Integer.valueOf(key) * 100));
        roomDto.setHotelId(hotel.getId());
        return roomDto;
    }

    private HotelDto createHotel(String seed, String city){
        HotelDto hotel = new HotelDto();
            hotel.setName(seed);
            hotel.setCity(city);
            hotel.setEmail(seed + "@hotel.com");
            hotel.setPhoneNumber("number: " + seed);
            hotel.setStreet("street: " + city + " " + seed);
            hotel.setStreetNumber("st number: " + seed.getBytes().length);
        return hotel;
    }
}
