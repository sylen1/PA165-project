package cz.muni.pa165.bookingmanager.application.service.iface;

import cz.muni.pa165.bookingmanager.iface.util.HotelStatistics;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.persistence.entity.HotelEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    /**
     * @see PageResult
     * @see PageInfo
     */
    PageResult<HotelEntity> findByCity(String city, PageInfo pageInfo);

    Optional<HotelEntity> findById(Long id);
}
