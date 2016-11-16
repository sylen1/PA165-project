package cz.muni.pa165.bookingmanager.persistence.dao;

import cz.muni.pa165.bookingmanager.persistence.entity.CustomerEntity;
import cz.muni.pa165.bookingmanager.persistence.entity.ReservationEntity;
import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

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

}
