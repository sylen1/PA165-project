/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pa165.bookingmanager.iface.util;

import cz.muni.pa165.bookingmanager.iface.dto.ReservationState;
import java.sql.Date;
import java.util.Optional;

/**
 * Simple POJO, that is used to specify restrictions for retrieving reservations
 * through findFiltered() method of ReservationFacade.
 * Leaving some parameters set to null means, that those will not impose any
 * restrictions on the result.
 * @author Mojmír Odehnal, 374422
 */
public class ReservationFilter {
    private ReservationState state;
    private Long roomId;
    private Long customerId;
    private Date startsBefore;
    private Date endsAfter;
    
    public Optional<ReservationState> getState() {
        return Optional.ofNullable(state);
    }

    public ReservationFilter setState(ReservationState state) {
        this.state = state;
        return this;
    }

    public Optional<Long> getRoomId() {
        return Optional.ofNullable(roomId);
    }

    public ReservationFilter setRoomId(Long roomId) {
        this.roomId = roomId;
        return this;
    }

    public Optional<Long> getCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public ReservationFilter setCustomerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    public Optional<Date> getStartsBefore() {
        return Optional.ofNullable(startsBefore);
    }

    public ReservationFilter setStartsBefore(Date startsBefore) {
        this.startsBefore = startsBefore;
        return this;
    }

    public Optional<Date> getEndsAfter() {
        return Optional.ofNullable(endsAfter);
    }

    public ReservationFilter setEndsAfter(Date endsAfter) {
        this.endsAfter = endsAfter;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReservationFilter that = (ReservationFilter) o;

        if (getState() != that.getState()) return false;
        if (getRoomId() != null ? !getRoomId().equals(that.getRoomId()) : that.getRoomId() != null) return false;
        if (getCustomerId() != null ? !getCustomerId().equals(that.getCustomerId()) : that.getCustomerId() != null)
            return false;
        if (getStartsBefore() != null ? !getStartsBefore().equals(that.getStartsBefore()) : that.getStartsBefore() != null)
            return false;
        return getEndsAfter() != null ? getEndsAfter().equals(that.getEndsAfter()) : that.getEndsAfter() == null;

    }

    @Override
    public int hashCode() {
        int result = getState() != null ? getState().hashCode() : 0;
        result = 31 * result + (getRoomId() != null ? getRoomId().hashCode() : 0);
        result = 31 * result + (getCustomerId() != null ? getCustomerId().hashCode() : 0);
        result = 31 * result + (getStartsBefore() != null ? getStartsBefore().hashCode() : 0);
        result = 31 * result + (getEndsAfter() != null ? getEndsAfter().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReservationFilter{" +
                "state=" + state +
                ", roomId=" + roomId +
                ", customerId=" + customerId +
                ", startsBefore=" + startsBefore +
                ", endsAfter=" + endsAfter +
                '}';
    }
}
