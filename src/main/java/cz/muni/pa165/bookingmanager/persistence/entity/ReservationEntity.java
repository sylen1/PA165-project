package cz.muni.pa165.bookingmanager.persistence.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

/**
 * Entity class for a hotel room reservation
 * @author Matej Harcar, 422714
 */
@Entity
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable=false)
    @ManyToOne
    @JoinColumn(name="hotel_id")
    private HotelEntity hotel;
    
    @Column(nullable=false)
    @ManyToOne
    @JoinColumn(name="room_id")
    private RoomEntity room;
    
    @Column(nullable=false)
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

    public HotelEntity getHotel() {
        return hotel;
    }

    public void setHotel(HotelEntity hotel) {
        this.hotel = hotel;
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

    @Override // Auto-generated
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.hotel);
        hash = 97 * hash + Objects.hashCode(this.room);
        hash = 97 * hash + Objects.hashCode(this.customer);
        hash = 97 * hash + Objects.hashCode(this.startDate);
        hash = 97 * hash + Objects.hashCode(this.endDate);
        return hash;
    }

    @Override // Auto-generated
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ReservationEntity)) {
            return false;
        }
        final ReservationEntity other = (ReservationEntity) obj;
        if (!Objects.equals(this.hotel, other.hotel)) {
            return false;
        }
        if (!Objects.equals(this.room, other.room)) {
            return false;
        }
        if (!Objects.equals(this.customer, other.customer)) {
            return false;
        }
        if (!Objects.equals(this.startDate, other.startDate)) {
            return false;
        }
        if (!Objects.equals(this.endDate, other.endDate)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ReservationEntity{" + "id=" + id + ", hotel=" + hotel 
                + ", room=" + room + ", customer=" + customer 
                + ", startDate=" + startDate + ", endDate=" + endDate + '}';
    }
    
    
    
}
