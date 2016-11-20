package cz.muni.pa165.bookingmanager.persistence.dao;

import cz.muni.pa165.bookingmanager.persistence.entity.CustomerEntity;
import cz.muni.pa165.bookingmanager.persistence.entity.ReservationEntity;
import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * DAO for reservations
 * @author Matej Harcar, 422714
 */
public interface ReservationDao extends JpaRepository<ReservationEntity, Long>{

    /**
     * Finds all reservations a given customer has made
     * @param c customer whose reservations are to be found
     * @return List containing all reservations made by given customer
     */
    List<ReservationEntity> findByCustomer(CustomerEntity c);

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
    
    // http://stackoverflow.com/a/39231964
    @Query("SELECT r FROM ReservationEntity r WHERE (:roomId IS NULL OR r.room.id = :roomId)"
        + " AND (:customerId IS NULL OR r.customer.id = :customerId)"
        + " AND (:startsBefore IS NULL OR r.startDate <= :startsBefore)"
        + " AND (:endsAfter IS NULL OR r.endDate >= :endsAfter) AND (:state IS NULL OR r.state = :state)")
    List<ReservationEntity> findByOptionalCustomCriteria( @Param("roomId") Long roomId, 
            @Param("customerId") Long customerId, @Param("startsBefore") Date startsBefore, 
            @Param("endsAfter") Date endsAfter, @Param("state") String state, Pageable pageable );
    
    @Query("SELECT COUNT(r.id) FROM ReservationEntity r WHERE (:roomId IS NULL OR r.room.id = :roomId)"
        + " AND (:customerId IS NULL OR r.customer.id = :customerId)"
        + " AND (:startsBefore IS NULL OR r.startDate <= :startsBefore)"
        + " AND (:endsAfter IS NULL OR r.endDate >= :endsAfter) AND (:state IS NULL OR r.state = :state)")
    Long countByOptionalCustomCriteria( @Param("roomId") Long roomId, 
            @Param("customerId") Long customerId, @Param("startsBefore") Date startsBefore, 
            @Param("endsAfter") Date endsAfter, @Param("state") String state );
}
