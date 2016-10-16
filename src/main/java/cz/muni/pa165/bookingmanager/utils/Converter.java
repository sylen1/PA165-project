package cz.muni.pa165.bookingmanager.utils;

import cz.muni.pa165.bookingmanager.persistence.entity.HotelEntity;
import cz.muni.pa165.bookingmanager.service.dto.HotelDto;

/**
 * Utility class containing static methods for object mapping.
 * */

public class Converter {

    private Converter() { }

    public static HotelDto entityToDto(HotelEntity entity){
        throw new UnsupportedOperationException("Method not implemented");
    }

    public static HotelEntity dtoToEntity(HotelDto dto){
        throw new UnsupportedOperationException("Method not implemented");
    }
}
