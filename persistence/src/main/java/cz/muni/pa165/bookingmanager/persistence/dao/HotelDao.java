package cz.muni.pa165.bookingmanager.persistence.dao;

import cz.muni.pa165.bookingmanager.persistence.entity.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data access layer for @{@link HotelEntity}
 *
 * @author Ludovit Labaj
 * */

public interface HotelDao extends JpaRepository<HotelEntity, Long> {

}
