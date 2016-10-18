package cz.muni.pa165.bookingmanager.utils;

import cz.muni.pa165.bookingmanager.persistence.entity.CustomerEntity;
import cz.muni.pa165.bookingmanager.persistence.entity.HotelEntity;
import cz.muni.pa165.bookingmanager.persistence.entity.ReservationEntity;
import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;
import cz.muni.pa165.bookingmanager.service.dto.CustomerDto;
import cz.muni.pa165.bookingmanager.service.dto.HotelDto;
import cz.muni.pa165.bookingmanager.service.dto.ReservationDto;
import cz.muni.pa165.bookingmanager.service.dto.RoomDto;

/**
 * Utility class containing static methods for object mapping.
 * */

public class Converter {

    private Converter() { }

    public static HotelDto hotelEntityToDto(HotelEntity entity){
        throw new UnsupportedOperationException("Method not implemented");
    }

    public static HotelEntity hotelDtoToEntity(HotelDto dto){
        throw new UnsupportedOperationException("Method not implemented");
    }
        
    public static CustomerDto customerEntityToDto(CustomerEntity entity){
        throw new UnsupportedOperationException("Method not implemented");
    }
    
    public static CustomerEntity customerDtoToEntity(CustomerDto dto){
        throw new UnsupportedOperationException("Method not implemented");
    }
    
    public static ReservationDto reservationEntityToDto(ReservationEntity entity){
        throw new UnsupportedOperationException("Method not implemented");
    }
    
    public static ReservationEntity reservationDtoToEntity(ReservationDto dto){
        throw new UnsupportedOperationException("Method not implemented");
    }

    public static RoomDto roomEntityToDto(RoomEntity roomEntity) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    public static RoomEntity roomDtoToEntity(RoomDto roomDto) {
        throw new UnsupportedOperationException("Method not implemented");
    }
}
