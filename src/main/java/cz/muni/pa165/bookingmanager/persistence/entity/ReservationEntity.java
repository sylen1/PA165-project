package cz.muni.pa165.bookingmanager.persistence.entity;

import javax.persistence.*;
import java.sql.Date;

/**
 * Entity class for a hotel room reservation
 * @author Matej Harcar, 422714
 */
@Entity
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="room_id")
    private RoomEntity room;
    
    @ManyToOne
    @JoinColumn(name="customer_id")
    private CustomerEntity customer;
    
    @Column(nullable=false)
    private Date startDate;
    
    @Column(nullable=false)
    private Date endDate;
 
    public ReservationEntity(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoomEntity getRoom() {
        return room;
    }

    public void setRoom(RoomEntity room) {
        this.room = room;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
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
        if (!(o instanceof ReservationEntity)) return false;

        ReservationEntity that = (ReservationEntity) o;

        if (!getRoom().equals(that.getRoom())) return false;
        if (!getCustomer().equals(that.getCustomer())) return false;
        if (!getStartDate().equals(that.getStartDate())) return false;
        return getEndDate().equals(that.getEndDate());

    }

    @Override
    public int hashCode() {
        int result = getRoom().hashCode();
        result = 31 * result + getCustomer().hashCode();
        result = 31 * result + getStartDate().hashCode();
        result = 31 * result + getEndDate().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ReservationEntity{" + "id=" + id + ", room=" + room 
                + ", customer=" + customer + ", startDate=" + startDate 
                + ", endDate=" + endDate + '}';
    }
    
    
}
