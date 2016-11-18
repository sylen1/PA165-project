package cz.muni.pa165.bookingmanager.application.service.iface;

import cz.muni.pa165.bookingmanager.iface.util.Page;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.persistence.entity.HotelEntity;

public interface HotelService extends PageableService<HotelEntity> {
    /**
     * Registers new hotel to the system
     * @param hotelEntity hotel to be saved
     * @return saved hotel instance
     */
    HotelEntity registerHotel(HotelEntity hotelEntity);

    /**
     * Updates information about existing hotel
     * @param hotelEntity hotel with updated information
     * @return updated hotel instance
     */
    HotelEntity updateHotelInformation(HotelEntity hotelEntity);

    /***
     * @see Page
     * @see PageInfo
     */
    Page<HotelEntity> findByCity(String city, PageInfo pageInfo);
}
