package cz.muni.pa165.bookingmanager.persistence.dao;

import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Data access layer for @{@link RoomEntity}
 * @Author Ondřej Gasior & Mojmír Odehnal
 */
public interface RoomDao extends JpaRepository<RoomEntity, Long> {
    RoomEntity findByName(String name);
    Page<RoomEntity> findByBedCountGreaterThanAndBedCountLessThanAndPriceGreaterThanAndPriceLessThan(int bedCountFrom, int bedCountTo, BigDecimal priceFrom, BigDecimal priceTo, Pageable pageable);
    int countByBedCountGreaterThanAndBedCountLessThanAndPriceGreaterThanAndPriceLessThan(int bedCountFrom, int bedCountTo, BigDecimal priceFrom, BigDecimal priceTo);
    long count();

    @Query("SELECT ro FROM RoomEntity ro"
            + " WHERE (:city = '' OR ro.id IN (SELECT ro2.id FROM HotelEntity h JOIN h.rooms ro2 WHERE h.city = :city))"
            + "   AND NOT EXISTS (" // does not have reservation in that time range
            + "     SELECT re.id FROM ReservationEntity re WHERE"
            + "       re.room.id = ro.id AND ("
            + "         (re.startDate < :since AND re.endDate > :until)" // is valid through the whole interval
            + "         OR (re.startDate >= :since AND re.startDate <= :until)" // or starts in it (and ends either in or out)
            + "         OR (re.endDate >= :since AND re.endDate <= :until)"  // or ends in it (and starts either in or out)
            + "       )"
            + "   )"
            + "   AND ro.bedCount > :bedsFrom AND ro.bedCount < :bedsTo AND ro.price > :priceFrom AND ro.price < :priceTo")
    Page<RoomEntity> findAvailableRooms(@Param("since") Date availableFrom, @Param("until") Date availableTo,
            @Param("bedsFrom") int bedCountFrom, @Param("bedsTo") int bedCountTo, @Param("priceFrom") BigDecimal priceFrom,
            @Param("priceTo") BigDecimal priceTo, @Param("city") String city, Pageable pageInfo);
}
