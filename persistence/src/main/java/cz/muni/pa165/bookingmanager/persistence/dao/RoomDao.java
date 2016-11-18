package cz.muni.pa165.bookingmanager.persistence.dao;

import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data access layer for @{@link RoomEntity}
 * @Author Ondřej Gasior
 */
public interface RoomDao extends JpaRepository<RoomEntity, Long> {
    RoomEntity findByName(String name);
    List<RoomEntity> findByBedCountGreaterThanAndBedCountLessThanAndPriceGreaterThanAndPriceLessThan(int bedCountFrom, int bedCountTo, BigDecimal priceFrom, BigDecimal priceTo, Pageable pageable);
    int countByBedCountGreaterThanAndBedCountLessThanAndPriceGreaterThanAndPriceLessThan(int bedCountFrom, int bedCountTo, BigDecimal priceFrom, BigDecimal priceTo);
    long count();

}
