package cz.muni.pa165.bookingmanager.persistence.entity;

import javax.persistence.*;

@Entity
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String token;

    public UserToken() {
    }

    public UserToken(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserToken)) return false;

        UserToken userToken = (UserToken) o;

        if (getEmail() != null ? !getEmail().equals(userToken.getEmail()) : userToken.getEmail() != null) return false;
        return getToken() != null ? getToken().equals(userToken.getToken()) : userToken.getToken() == null;

    }

    @Override
    public int hashCode() {
        int result = getEmail() != null ? getEmail().hashCode() : 0;
        result = 31 * result + (getToken() != null ? getToken().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserToken{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
