package cz.muni.pa165.bookingmanager.persistence.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

/**
 * Entity class for a hotel room reservation
 * @author Matej Harcar, 422714 & Mojmír Odehnal, 374422
 */
@Entity
@Table(name = "RESERVATION")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="room_id",nullable = false)
    private RoomEntity room;
    
    @ManyToOne
    @JoinColumn(name="customer_id", nullable = false)
    private UserEntity customer;
    
    @Column(nullable=false)
    private Date startDate;
    
    @Column(nullable=false)
    private Date endDate;
    
    @Column(nullable=false)
    private String state;
 
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

    public UserEntity getCustomer() {
        return customer;
    }

    public void setCustomer(UserEntity customer) {
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationEntity)) return false;

        ReservationEntity that = (ReservationEntity) o;

        if (!Objects.equals(getRoom(), that.getRoom())) return false;
        if (!Objects.equals(getCustomer(), that.getCustomer())) return false;
        if (!Objects.equals(getStartDate(),that.getStartDate())) return false;
        if (!Objects.equals(getEndDate(), that.getEndDate())) return false;
        return Objects.equals(getState(), that.getState());

    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getRoom());
        result = 31 * result + Objects.hashCode(getCustomer());
        result = 31 * result + Objects.hashCode(getStartDate());
        result = 31 * result + Objects.hashCode(getEndDate());
        result = 31 * result + Objects.hashCode(getState());
        return result;
    }

    @Override
    public String toString() {
        return "ReservationEntity{" + "id=" + id + ", room=" + room 
                + ", customer=" + customer + ", startDate=" + startDate 
                + ", endDate=" + endDate + ", state=" + state + '}';
    }
    
    
}
