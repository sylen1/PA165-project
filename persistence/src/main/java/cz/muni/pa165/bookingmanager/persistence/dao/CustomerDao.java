package cz.muni.pa165.bookingmanager.persistence.dao;

import cz.muni.pa165.bookingmanager.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DAO class for accessing customer data in DB
 * @author Matej Harcar, 422714
 */
public interface CustomerDao extends JpaRepository<CustomerEntity, Long>{
    
}
