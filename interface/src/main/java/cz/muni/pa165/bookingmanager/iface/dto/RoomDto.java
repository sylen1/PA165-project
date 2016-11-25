package cz.muni.pa165.bookingmanager.iface.dto;

import java.math.BigDecimal;

/**
 * @author Gasior
 */
public class RoomDto {

    private Long id;

    private String name;

    private BigDecimal price;

    private int bedCount;

    private String description;

    private long hotelId;

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

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoomDto)) return false;

        RoomDto roomDto = (RoomDto) o;

        if (!getName().equals(roomDto.getName())) return false;
        return getHotelId() == roomDto.getHotelId();

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RoomDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", bedCount=" + bedCount +
                ", description='" + description + '\'' +
                ", hotelId=" + hotelId +
                '}';
    }
}
