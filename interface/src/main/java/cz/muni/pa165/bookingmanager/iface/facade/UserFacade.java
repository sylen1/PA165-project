package cz.muni.pa165.bookingmanager.iface.facade;

import cz.muni.pa165.bookingmanager.iface.dto.UserDto;
import cz.muni.pa165.bookingmanager.iface.dto.UserLoginDto;
import cz.muni.pa165.bookingmanager.iface.util.Page;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;

import java.util.Optional;

/**
 * User facade interface declaration
 * @author Matej Harcar, 422714
 */
public interface UserFacade extends PageableFacade<UserDto>{

    /**
     * Finds user by email address
     * @param email email to search for
     * @return Optional instance with same email as given if found, else an empty one
     */
    public Optional<UserDto> findByEmail(String email);
    // typical user does not remember ID, nor do we need it since emails are unique

    /**
     * Attempts to authenticate user
     * @param u UserLoginDto instance with email and password given by user
     * @return true on success, false on failure(wrong info)
     */
    public boolean authenticate(UserLoginDto u);

    /**
     * Attempts to register a user in the database
     * @param user UserDto instance containing user info
     * @param passwd plain-text password chosen by user
     * @return true on success, false on failure(eg. email already registered)
     */
    public boolean registerUser(UserDto user, String passwd);

    /**
     * Checks whether a given user is an administrator
     * @param u user to check up on
     * @return Admin flag setting for given user (whether they're an admin)
     */
    public boolean isAdmin(UserDto u);

    /**
     * Finds all users and gives a page of results
     * @param pageInfo PageInfo instance to help define the result
     * @return Page instance containing UserDto instances
     */
    @Override
    public Page<UserDto> findAll(PageInfo pageInfo);

    /**
     * Updates user info(eg. when user moves)
     * @param user new user info to be saved
     * @return UserDto instance with updated user info
     */
    public UserDto updateUser(UserDto user);

}
