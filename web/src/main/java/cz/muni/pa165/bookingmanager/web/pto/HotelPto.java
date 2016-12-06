package cz.muni.pa165.bookingmanager.web.pto;

import cz.muni.pa165.bookingmanager.iface.util.HotelStatistics;

public class HotelPto {
    private Long id;

    private String name;

    private String city;

    private String street;

    private String streetNumber;

    private String email;

    private String phoneNumber;

    private HotelStatistics hotelStatistics;

    private List<RoomPto> rooms;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HotelPto hotelPto = (HotelPto) o;

        if (id != null ? !id.equals(hotelPto.id) : hotelPto.id != null) return false;
        if (name != null ? !name.equals(hotelPto.name) : hotelPto.name != null) return false;
        if (city != null ? !city.equals(hotelPto.city) : hotelPto.city != null) return false;
        if (street != null ? !street.equals(hotelPto.street) : hotelPto.street != null) return false;
        if (streetNumber != null ? !streetNumber.equals(hotelPto.streetNumber) : hotelPto.streetNumber != null)
            return false;
        if (email != null ? !email.equals(hotelPto.email) : hotelPto.email != null) return false;
        return phoneNumber != null ? phoneNumber.equals(hotelPto.phoneNumber) : hotelPto.phoneNumber == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (streetNumber != null ? streetNumber.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HotelPto{" +
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
