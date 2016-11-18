package cz.muni.pa165.bookingmanager.application.Utils;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Created by gasior on 18.11.2016.
 */
public class RoomFilter {

    private Optional<Integer> bedContFrom;
    private Optional<Integer> bedCountTo;
    private Optional<BigDecimal> priceFrom;
    private Optional<BigDecimal> priceTo;

    public Optional<Integer> getBedContFrom() {
        return bedContFrom;
    }

    public void setBedContFrom(Optional<Integer> bedContFrom) {
        this.bedContFrom = bedContFrom;
    }

    public Optional<Integer> getBedCountTo() {
        return bedCountTo;
    }

    public void setBedCountTo(Optional<Integer> bedCountTo) {
        this.bedCountTo = bedCountTo;
    }

    public Optional<BigDecimal> getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(Optional<BigDecimal> priceFrom) {
        this.priceFrom = priceFrom;
    }

    public Optional<BigDecimal> getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(Optional<BigDecimal> priceTo) {
        this.priceTo = priceTo;
    }
}
