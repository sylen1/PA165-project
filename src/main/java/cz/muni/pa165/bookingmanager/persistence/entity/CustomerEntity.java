package cz.muni.pa165.bookingmanager.persistence.entity;

import java.util.Objects;
import javax.persistence.*;


/**
 * Entity class for a customer wanting to book a hotel room
 * @author Matej Harcar, 422714
 */
public class CustomerEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable=false)
    private String name;
    
    @Column(nullable=false)
    private String address;
    
    @Column(unique=true,nullable=false)
    private String email;
    
    @Column(unique=true,nullable=false)
    private String phoneNumber;
    
    public CustomerEntity(){}

    public Long getId() {
        return id;
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

    @Override // Auto-generated
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.email);
        hash = 41 * hash + Objects.hashCode(this.phoneNumber);
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
        if (!(obj instanceof CustomerEntity)) {
            return false;
        }
        final CustomerEntity other = (CustomerEntity) obj;
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.phoneNumber, other.phoneNumber)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CustomerEntity{" + "id=" + id 
                + ", name=" + name + ", address=" + address 
                + ", email=" + email + ", phoneNumber=" + phoneNumber + '}';
    }
    
    
    
}
