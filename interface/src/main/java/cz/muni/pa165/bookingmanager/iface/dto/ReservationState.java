package cz.muni.pa165.bookingmanager.iface.dto;

/**
 * @author Matej Harcar, 422714
 */
public enum ReservationState {
    NEW, // Just made, unpaid
    UNPAID, // Guest is/was accomodated, has not paid yet, is within deadline for payment
    PAID, // Paid before accomodation
    OVERDUE, // Unpaid after payment expected
    CANCELLED,
    ENDED // Accomodation ended, payment received
}
