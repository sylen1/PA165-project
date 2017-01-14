package cz.muni.pa165.bookingmanager.persistence.dao;

import cz.muni.pa165.bookingmanager.persistence.entity.UserEntity;
import cz.muni.pa165.bookingmanager.persistence.entity.ReservationEntity;
import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * DAO for reservations
 * @author Matej Harcar, 422714 & Mojmír Odehnal, 374422
 */
public interface ReservationDao extends JpaRepository<ReservationEntity, Long>{

    /**
     * Finds all reservations a given customer has made
     * @param c customer whose reservations are to be found
     * @return List containing all reservations made by given customer
     */
    List<ReservationEntity> findByCustomer(UserEntity c);

    /**
     * Finds all reservations on a given room
     * @param r room whose reservations are to be found
     * @return List containing all reservations made for a given room
     */
    List<ReservationEntity> findByRoom(RoomEntity r);

    /**
     * Finds all reservations with start date before a given date and end date after another given date
     * @param date1 first given date, start date of reservation before this
     * @param date2 second given date, reservation end date after this
     * @return List containing reservations satisfying above criterium
     */
    List<ReservationEntity> findByStartDateBeforeAndEndDateAfter(Date date1, Date date2);
    
    long count();

    @Query("SELECT r FROM ReservationEntity r WHERE (:roomId IS NULL OR r.room.id = :roomId)"
        + " AND (:customerId IS NULL OR r.customer.id = :customerId)"
        + " AND (:startsBefore IS NULL OR r.startDate <= :startsBefore)"
        + " AND (:endsAfter IS NULL OR r.endDate >= :endsAfter) AND (:state IS NULL OR r.state = :state)")
    Page<ReservationEntity> findByOptionalCustomCriteria(@Param("roomId") Long roomId,
             @Param("customerId") Long customerId, @Param("startsBefore") Date startsBefore,
             @Param("endsAfter") Date endsAfter, @Param("state") String state, Pageable pageable );

    @Query("SELECT COUNT(r.id) FROM ReservationEntity r WHERE (:roomId IS NULL OR r.room.id = :roomId)"
        + " AND (:customerId IS NULL OR r.customer.id = :customerId)"
        + " AND (:startsBefore IS NULL OR r.startDate <= :startsBefore)"
        + " AND (:endsAfter IS NULL OR r.endDate >= :endsAfter) AND (:state IS NULL OR r.state = :state)")
    Long countByOptionalCustomCriteria( @Param("roomId") Long roomId,
            @Param("customerId") Long customerId, @Param("startsBefore") Date startsBefore,
            @Param("endsAfter") Date endsAfter, @Param("state") String state );

    @Query("SELECT COUNT(re.id) FROM ReservationEntity re"
            + " WHERE re.room.id = :roomId AND re.state <> 'CANCELLED'"
            + "   AND ("
            + "     (re.startDate < :intFrom AND re.endDate > :intTo)" // is valid through the whole interval
            + "     OR (re.startDate >= :intFrom AND re.startDate <= :intTo)" // or starts in it (and ends either in or out)
            + "     OR (re.endDate >= :intFrom AND re.endDate <= :intTo)"  // or ends in it (and starts either in or out)
            + "   )")
    Long countByRoomIdValidInInterval(@Param("roomId") Long roomId,
             @Param("intFrom") Date validInIntervalFrom, @Param("intTo") Date validInIntervalTo);

    @Query("SELECT re FROM ReservationEntity re"
            + " WHERE re.room.id IN (SELECT ro.id FROM HotelEntity h JOIN h.rooms ro WHERE h.id = :hotelId)"
            + "   AND ("
            + "     (re.startDate < :intFrom AND re.endDate > :intTo)" // is valid through the whole interval
            + "     OR (re.startDate >= :intFrom AND re.startDate <= :intTo)" // or starts in it (and ends either in or out)
            + "     OR (re.endDate >= :intFrom AND re.endDate <= :intTo)"  // or ends in it (and starts either in or out)
            + "   )")
    List<ReservationEntity> findByHotelIdValidInInterval(@Param("hotelId") Long hotelId,
            @Param("intFrom") Date validInIntervalFrom, @Param("intTo") Date validInIntervalTo);
}
