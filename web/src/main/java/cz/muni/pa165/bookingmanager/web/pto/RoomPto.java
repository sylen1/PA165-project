package cz.muni.pa165.bookingmanager.web.pto;

import cz.muni.pa165.bookingmanager.iface.dto.RoomDto;

import java.math.BigDecimal;

/**
 * Created by gasior on 06.12.2016.
 */
public class RoomPto {

    private Long id;

    private String name;

    private BigDecimal price;

    private int bedCount;

    private String description;

    private Long hotelId;

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

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoomDto)) return false;

        RoomDto roomDto = (RoomDto) o;

        if (!getName().equals(roomDto.getName())) return false;
        return getHotelId().equals(roomDto.getHotelId());

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + hotelId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RoomPto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", bedCount=" + bedCount +
                ", description='" + description + '\'' +
                ", hotelId=" + hotelId +
                '}';
    }
}
