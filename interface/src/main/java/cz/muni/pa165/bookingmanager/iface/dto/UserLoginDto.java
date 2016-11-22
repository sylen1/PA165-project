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
}
