package cz.muni.pa165.bookingmanager.iface.dto;

import java.util.List;

public class HotelDto {
    private Long id;
    private String name;
    private String city;

    private String street;

    private String streetNumber;

    private String email;

    private String phoneNumber;

    private List<RoomDto> rooms;

    public HotelDto() {
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

    public List<RoomDto> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomDto> rooms) {
        this.rooms = rooms;
    }

    public void addRoom(RoomDto room) {
        this.rooms.add(room);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HotelDto hotelDto = (HotelDto) o;

        if (getName() != null ? !getName().equals(hotelDto.getName()) : hotelDto.getName() != null) return false;
        if (getCity() != null ? !getCity().equals(hotelDto.getCity()) : hotelDto.getCity() != null) return false;
        if (getStreet() != null ? !getStreet().equals(hotelDto.getStreet()) : hotelDto.getStreet() != null)
            return false;
        return getStreetNumber() != null ? getStreetNumber().equals(hotelDto.getStreetNumber()) : hotelDto.getStreetNumber() == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getCity() != null ? getCity().hashCode() : 0);
        result = 31 * result + (getStreet() != null ? getStreet().hashCode() : 0);
        result = 31 * result + (getStreetNumber() != null ? getStreetNumber().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HotelDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", rooms=" + rooms +
                '}';
    }
}
