package cz.muni.pa165.bookingmanager.persistence.dao;

import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

/**
 * Data access layer for @{@link RoomEntity}
 * @Author Ondřej Gasior
 */
public interface RoomDao extends JpaRepository<RoomEntity, Long> {
    RoomEntity findByName(String name);
    Page<RoomEntity> findByBedCountGreaterThanAndBedCountLessThanAndPriceGreaterThanAndPriceLessThan(int bedCountFrom, int bedCountTo, BigDecimal priceFrom, BigDecimal priceTo, Pageable pageable);
    int countByBedCountGreaterThanAndBedCountLessThanAndPriceGreaterThanAndPriceLessThan(int bedCountFrom, int bedCountTo, BigDecimal priceFrom, BigDecimal priceTo);
    long count();

}
