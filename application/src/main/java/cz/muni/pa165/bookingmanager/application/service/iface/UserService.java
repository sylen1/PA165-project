package cz.muni.pa165.bookingmanager.application.service.iface;


import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.persistence.entity.UserEntity;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

/**
 * User service interface declaration
 * @author Matej Harcar, 422714
 */
public interface UserService extends PageableService<UserEntity> {

    /**
     * Finds all users and gives a page of results
     * @param pageInfo info to help define the result
     * @return PageResult containing UserDto instances
     */
    @Override
    PageResult<UserEntity> findAll(PageInfo pageInfo);

    /**
     * Attempts to find a user by email address
     * @param email email to search for
     * @return Optional instance, with UserDto if found, empty otherwise
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Attempts to register a new user
     * @param user UserDto instance containing user info
     * @param passwd plain-text password chosen by user
     * @return True on success, false on failure(eg. email already registered)
     */
    Pair<UserEntity,String> registerUser(UserEntity user, String passwd);

    /**
     * Checks whether given user is an admin
     * @param user user to check up on
     * @return Admin flag setting for given user
     */
    boolean isAdmin(UserEntity user);

    /**
     * Attempts to authenticate a user
     * @param u UserEntity with relevant info
     * @param passwd Password to check
     * @return True on success, false on failure
     */
    boolean authenticate(UserEntity user, String passwd);

    /**
     * Updates user info
     * @param user New user info to be saved
     * @return UserDto instance with updated user info
     */
    UserEntity updateUser(UserEntity user);


    /**
     * confirms user's email adress
     * @param email user's email adress
     * @param token token for user confirmation
     * @return if confirmation is successful
     */
    boolean confirmUser(String email, String token);
}
