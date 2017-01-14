package cz.muni.pa165.bookingmanager.web.pto;


import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

public class UserRegistrationPto {
    @Length(min = 2, max = 32)
    private String name;
    @NotBlank
    private String address;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String phoneNumber;
    @Past
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @NotNull
    private Date birthDate;
    @Length(min = 6, max = 32)
    private String password;

    public UserRegistrationPto() {
    }

    public String getName() {
        return name;
    }

    public UserRegistrationPto setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public UserRegistrationPto setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRegistrationPto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserRegistrationPto setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public UserRegistrationPto setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRegistrationPto setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRegistrationPto that = (UserRegistrationPto) o;

        return email != null ? email.equals(that.email) : that.email == null;

    }

    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserRegistrationPto{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthDate=" + birthDate +
                ", password='" + (password == null ? "null" : "********") + "'" +
                '}';
    }
}
