package cz.muni.pa165.bookingmanager.persistence.entity;

/**
 * User account state enum
 * @author Matej Harcar, 422714
 */
public enum DatabaseAccountState {
    INACTIVE, // new, unconfirmed user or one that requested account de-activation
    CUSTOMER, // regular customer
    ADMIN     // administrator
}
