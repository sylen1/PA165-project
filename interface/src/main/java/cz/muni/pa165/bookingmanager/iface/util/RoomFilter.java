package cz.muni.pa165.bookingmanager.iface.util;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Created by gasior on 18.11.2016.
 */
public class RoomFilter {

    private Integer bedCountFrom;
    private Integer bedCountTo;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;

    public RoomFilter(Integer bedCountFrom, Integer bedCountTo, BigDecimal priceFrom, BigDecimal priceTo) {
        this.bedCountFrom = bedCountFrom;
        this.bedCountTo = bedCountTo;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
    }

    public RoomFilter() {
    }

    public Optional<Integer> getBedCountFrom() {
        return Optional.ofNullable(bedCountFrom);
    }

    public RoomFilter setBedCountFrom(Integer bedCountFrom) {
        this.bedCountFrom = bedCountFrom;
        return this;
    }

    public Optional<Integer> getBedCountTo() {
        return Optional.ofNullable(bedCountTo);
    }

    public RoomFilter setBedCountTo(Integer bedCountTo) {
        this.bedCountTo = bedCountTo;
        return this;
    }

    public Optional<BigDecimal> getPriceFrom() {
        return Optional.ofNullable(priceFrom);
    }

    public RoomFilter setPriceFrom(BigDecimal priceFrom) {
        this.priceFrom = priceFrom;
        return this;
    }

    public Optional<BigDecimal> getPriceTo() {
        return Optional.ofNullable(priceTo);
    }

    public RoomFilter setPriceTo(BigDecimal priceTo) {
        this.priceTo = priceTo;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomFilter that = (RoomFilter) o;

        if (getBedCountFrom() != null ? !getBedCountFrom().equals(that.getBedCountFrom()) : that.getBedCountFrom() != null)
            return false;
        if (getBedCountTo() != null ? !getBedCountTo().equals(that.getBedCountTo()) : that.getBedCountTo() != null)
            return false;
        if (getPriceFrom() != null ? !getPriceFrom().equals(that.getPriceFrom()) : that.getPriceFrom() != null)
            return false;
        return getPriceTo() != null ? getPriceTo().equals(that.getPriceTo()) : that.getPriceTo() == null;

    }

    @Override
    public int hashCode() {
        int result = getBedCountFrom() != null ? getBedCountFrom().hashCode() : 0;
        result = 31 * result + (getBedCountTo() != null ? getBedCountTo().hashCode() : 0);
        result = 31 * result + (getPriceFrom() != null ? getPriceFrom().hashCode() : 0);
        result = 31 * result + (getPriceTo() != null ? getPriceTo().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RoomFilter{" +
                "bedCountFrom=" + bedCountFrom +
                ", bedCountTo=" + bedCountTo +
                ", priceFrom=" + priceFrom +
                ", priceTo=" + priceTo +
                '}';
    }
}
