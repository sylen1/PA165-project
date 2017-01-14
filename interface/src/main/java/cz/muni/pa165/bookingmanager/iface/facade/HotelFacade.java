package cz.muni.pa165.bookingmanager.iface.facade;

import cz.muni.pa165.bookingmanager.iface.dto.HotelDto;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;

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

    PageResult<HotelDto> findByCity(String city, PageInfo pageInfo);

    Optional<HotelDto> findById(Long id);

    Optional<HotelDto> findByName(String name);
}
