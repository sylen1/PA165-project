package cz.muni.pa165.bookingmanager.persistence.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * Entity represents hotel, containing rooms which can be reserved by customer.
 *
 * @author Ludovit Labaj
 * */

@Entity
@Table(name = "HOTEL")
public class HotelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String streetNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id")
    private Set<RoomEntity> rooms;

    public HotelEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<RoomEntity> getRooms() {
        return rooms;
    }

    public void setRooms(Set<RoomEntity> rooms) {
        this.rooms = rooms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HotelEntity)) return false;

        HotelEntity that = (HotelEntity) o;

        if (!Objects.equals(getName(), that.getName())) return false;
        if (!Objects.equals(getCity(), that.getCity())) return false;
        if (!Objects.equals(getStreet(), that.getStreet())) return false;
        return Objects.equals(getStreetNumber(), that.getStreetNumber());

    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getName());
        result = 31 * result + Objects.hashCode(getCity());
        result = 31 * result + Objects.hashCode(getStreet());
        result = 31 * result + Objects.hashCode(getStreetNumber());
        return result;
    }

    @Override
    public String toString() {
        return "HotelEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
