package cz.muni.pa165.bookingmanager.iface.dto;

/**
 * @author Matej Harcar, 422714
 */
public enum ReservationState {
    NEW, // Just made, unpaid
    CONFIRMED,
    PAID, // Paid before accomodation
    CANCELLED,
    ENDED // Accomodation ended, payment received
}
