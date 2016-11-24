package cz.muni.pa165.bookingmanager.iface.dto;

/**
 * Simple class holding user info at login
 * @author Matej Harcar, 422714
 */
public class UserLoginDto {
    private String email;
    private String passwd;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserLoginDto that = (UserLoginDto) o;

        if (getEmail() != null ? !getEmail().equals(that.getEmail()) : that.getEmail() != null) return false;
        return getPasswd() != null ? getPasswd().equals(that.getPasswd()) : that.getPasswd() == null;

    }

    @Override
    public int hashCode() {
        int result = getEmail() != null ? getEmail().hashCode() : 0;
        result = 31 * result + (getPasswd() != null ? getPasswd().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserLoginDto{" +
                "email='" + email + '\'' +
                ", passwd='" + passwd + '\'' +
                '}';
    }
}
