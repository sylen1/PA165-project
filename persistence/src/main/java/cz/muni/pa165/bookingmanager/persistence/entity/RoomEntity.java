package cz.muni.pa165.bookingmanager.persistence.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * RoomEntity is representation of room in a hotel building. Room name is unique ONLY in one hotel.
 * @Author Ondřej Gasior
 */
@Entity
@Table(name = "ROOM")
public class RoomEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name; //LIKE A320

    @Column(nullable=false)
    private BigDecimal price;

    @Column(nullable=false)
    private int bedCount;

    @Column
    private String description;

    @Column(nullable=false)
    private Long hotelId;


    public RoomEntity() {

    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getBedCount() {
        return bedCount;
    }

    public void setBedCount(int bedCount) {
        this.bedCount = bedCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomEntity that = (RoomEntity) o;

        if (!name.equals(that.name)) return false;
        return hotelId.equals(that.hotelId);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + hotelId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RoomEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", bedCount=" + bedCount +
                ", description='" + description + '\'' +
                ", hotelId=" + hotelId +
                '}';
    }
}
