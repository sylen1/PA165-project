package cz.muni.pa165.bookingmanager.persistence.dao;

import cz.muni.pa165.bookingmanager.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * DAO class for accessing customer data in DB
 * @author Matej Harcar, 422714
 */
public interface UserDao extends JpaRepository<UserEntity, Long>{

    /**
     * Finds all customers with a given name
     * - useful eg. if customer forgets email
     * @param name name to look up
     * @return List of customers with given name
     */
    List<UserEntity> findByName(String name);

    /**
     * Finds user by email
     * @param email email to search for
     * @return UserEntity instance if found, else null
     */
    UserEntity findByEmail(String email);

}
