package cz.muni.pa165.bookingmanager.iface.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;


/**
 * Simple POJO carrying some hotel statistics
 * @author Mojmír Odehnal, 374422
 */
public class HotelStatistics {

    long numberOfCompletedReservations;
    BigDecimal revenue;
    double averageRoomUsage;
    double averageReservationLength;

    public HotelStatistics(long numberOfCompletedReservations, BigDecimal revenue, double averageRoomUsage, double averageReservationLength) {
        this.numberOfCompletedReservations = numberOfCompletedReservations;
        this.revenue = revenue;
        this.averageRoomUsage = averageRoomUsage;
        this.averageReservationLength = averageReservationLength;
    }

    public String getUsage() {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(averageRoomUsage*100)+"%";
    }

    public String getReservationLenght() {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(averageReservationLength);
    }
    public long getNumberOfCompletedReservations() {
        return numberOfCompletedReservations;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public double getAverageRoomUsage() {
        return averageRoomUsage;
    }

    public double getAverageReservationLength() {
        return averageReservationLength;
    }

    @Override
    public String toString() {
        return "HotelStatistics{" +
                "numberOfCompletedReservations=" + numberOfCompletedReservations +
                ", revenue=" + revenue +
                ", averageRoomUsage=" + averageRoomUsage +
                ", averageReservationLength=" + averageReservationLength +
                '}';
    }
}
