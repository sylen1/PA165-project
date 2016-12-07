package cz.muni.pa165.bookingmanager.web.pto;

import cz.muni.pa165.bookingmanager.iface.dto.ReservationState;

import java.util.Date;

/**
 * Representation of reservation on presentation layer
 * @author Mojmír Odehnal, 374422
 */
public class ReservationPto {
    
    private ReservationState state;
    
    private Long id;
    
    private RoomPto room;
    
    private UserPto customer;
    
    private Date startDate;
    
    private Date endDate;

    public ReservationState getState() {
        return state;
    }

    public void setState(ReservationState state) {
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoomPto getRoom() {
        return room;
    }

    public void setRoom(RoomPto room) {
        this.room = room;
    }

    public UserPto getCustomer() {
        return customer;
    }

    public void setCustomer(UserPto customer) {
        this.customer = customer;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReservationPto that = (ReservationPto) o;

        if (getState() != that.getState()) return false;
        if (getRoom() != null ? !getRoom().equals(that.getRoom()) : that.getRoom() != null) return false;
        if (getCustomer() != null ? !getCustomer().equals(that.getCustomer()) : that.getCustomer() != null)
            return false;
        if (getStartDate() != null ? !getStartDate().equals(that.getStartDate()) : that.getStartDate() != null)
            return false;
        return getEndDate() != null ? getEndDate().equals(that.getEndDate()) : that.getEndDate() == null;

    }

    @Override
    public int hashCode() {
        int result = getState() != null ? getState().hashCode() : 0;
        result = 31 * result + (getRoom() != null ? getRoom().hashCode() : 0);
        result = 31 * result + (getCustomer() != null ? getCustomer().hashCode() : 0);
        result = 31 * result + (getStartDate() != null ? getStartDate().hashCode() : 0);
        result = 31 * result + (getEndDate() != null ? getEndDate().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReservationPto{" +
                "id=" + id +
                ", state=" + state +
                ", room=" + room +
                ", customer=" + customer +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
