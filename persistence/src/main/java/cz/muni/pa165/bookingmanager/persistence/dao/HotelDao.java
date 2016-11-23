package cz.muni.pa165.bookingmanager.persistence.dao;

import cz.muni.pa165.bookingmanager.persistence.entity.HotelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Data access layer for @{@link HotelEntity}
 *
 * @author Ludovit Labaj
 * */

@Repository
public interface HotelDao extends JpaRepository<HotelEntity, Long> {

    Page<HotelEntity> findByCity(String city, Pageable pageable);
}
