package cz.muni.pa165.bookingmanager.persistence.dao;

import cz.muni.pa165.bookingmanager.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * DAO class for accessing customer data in DB
 * @author Matej Harcar, 422714
 */
public interface CustomerDao extends JpaRepository<CustomerEntity, Long>{

    /**
     * Finds all customers with a given name
     * - useful eg. if customer forgets email
     * @param name name to look up
     * @return List of customers with given name
     */
    List<CustomerEntity> findByName(String name);

}
