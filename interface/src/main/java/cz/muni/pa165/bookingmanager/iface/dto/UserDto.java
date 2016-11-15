package cz.muni.pa165.bookingmanager.iface.dto;

import java.sql.Date;
import java.util.Arrays;

public class UserDto {

    private Long id;

    private String name;

    private String address;

    private String email;

    private String phoneNumber;

    private Date birthDate;

    private byte[] passwordHash;

    private byte[] passwordSalt;

    public UserDto(){}

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(byte[] passwordHash) {
        this.passwordHash = passwordHash;
    }

    public byte[] getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(byte[] passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof UserDto)) return false;

        UserDto userDto = (UserDto) o;

        if (!getEmail().equals(userDto.getEmail())) return false;
        if (!getPhoneNumber().equals(userDto.getPhoneNumber())) return false;
        if (!Arrays.equals(getPasswordHash(), userDto.getPasswordHash())) return false;
        return Arrays.equals(getPasswordSalt(), userDto.getPasswordSalt());
    }

    @Override
    public int hashCode() {
        int result = getEmail().hashCode();
        result = 31 * result + getPhoneNumber().hashCode();
        result = 31 * result + Arrays.hashCode(getPasswordHash());
        result = 31 * result + Arrays.hashCode(getPasswordSalt());
        return result;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
