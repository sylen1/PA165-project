package cz.muni.pa165.bookingmanager.persistence.dao;

import cz.muni.pa165.bookingmanager.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DAO class for accessing customer data in DB
 * @author Matej Harcar, 422714
 */
public interface UserDao extends JpaRepository<UserEntity, Long>{

    /**
     * Finds user by email
     * @param email email to search for
     * @return UserEntity instance if found, else null
     */
    UserEntity findByEmail(String email);

    /**
     * Finds user by phone
     * @param phone phone number to look for
     * @return UserEntity instance if found, else null
     */
    UserEntity findByPhoneNumber(String phone);

}
