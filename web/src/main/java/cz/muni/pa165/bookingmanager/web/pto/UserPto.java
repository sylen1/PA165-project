package cz.muni.pa165.bookingmanager.web.pto;

import cz.muni.pa165.bookingmanager.iface.dto.AccountState;

import java.util.Date;

/**
 * @author Matej Harcar, 422714
 */
public class UserPto {

    private Long id;

    private String name;

    private String address;

    private String email;

    private String phoneNumber;

    private Date birthDate;

    private AccountState accountState;

    public UserPto(){}

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

    public AccountState getAccountState() {
        return accountState;
    }

    public void setAccountState(AccountState accountState) {
        this.accountState = accountState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPto)) return false;

        UserPto userPto = (UserPto) o;

        if (!getEmail().equals(userPto.getEmail())) return false;
        return getPhoneNumber().equals(userPto.getPhoneNumber());

    }

    @Override
    public int hashCode() {
        int result = getEmail().hashCode();
        result = 31 * result + getPhoneNumber().hashCode();
        return result;
    }
}
