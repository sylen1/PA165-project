package cz.muni.pa165.bookingmanager.iface.dto;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Created by gasior on 18.11.2016.
 */
public class RoomFilter {

    private Integer bedContFrom;
    private Integer bedCountTo;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;

    public RoomFilter() {
    }

    public Optional<Integer> getBedContFrom() {
        return Optional.ofNullable(bedContFrom);
    }

    public RoomFilter setBedContFrom(Integer bedContFrom) {
        this.bedContFrom = bedContFrom;
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
}
