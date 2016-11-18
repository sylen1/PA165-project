package cz.muni.pa165.bookingmanager.iface.facade;

import cz.muni.pa165.bookingmanager.iface.dto.HotelDto;
import cz.muni.pa165.bookingmanager.iface.dto.RoomDto;
import cz.muni.pa165.bookingmanager.iface.util.Page;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;

import java.util.List;
import java.util.Optional;

public interface HotelFacade extends PageableFacade<HotelDto> {

    /**
     * Registers new hotel to the system
     * @param hotelDto hotel to be saved
     * @return saved hotel instance
     */
    HotelDto registerHotel(HotelDto hotelDto);

    /**
     * Updates information about existing hotel
     * @param hotelDto hotel with updated information
     * @return updated hotel instance
     */
    HotelDto updateHotelInformation(HotelDto hotelDto);

    Page<HotelDto> findByCity(String city, PageInfo pageInfo);

    Optional<HotelDto> findById(Long id);
}
