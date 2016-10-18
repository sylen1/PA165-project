package cz.muni.pa165.bookingmanager.persistence.dao;

import cz.muni.pa165.bookingmanager.persistence.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DAO for reservations
 * @author Matej Harcar, 422714
 */
public interface ReservationDao extends JpaRepository<ReservationEntity, Long>{

}
