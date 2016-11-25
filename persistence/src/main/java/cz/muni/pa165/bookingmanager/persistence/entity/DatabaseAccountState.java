package cz.muni.pa165.bookingmanager.persistence.entity;

/**
 * User account state enum
 * @author Matej Harcar, 422714
 */
public enum DatabaseAccountState {
    INACTIVE, // new, unconfirmed user
    CUSTOMER, // regular customer
    ADMIN     // administrator
}
