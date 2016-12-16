package cz.muni.pa165.bookingmanager.web.pto;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by gasior on 16.12.2016.
 */
public class RoomFilterPto {

    private Integer bedFrom;
    private Integer bedTo;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date dateFrom;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date dateTo;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private List<String> cities;
    private String city;

    public RoomFilterPto() {
    }

    public RoomFilterPto(Integer bedFrom, Integer bedTo, Date dateFrom, Date dateTo, BigDecimal priceFrom, BigDecimal priceTo, List<String> cities, String city) {
        this.bedFrom = bedFrom;
        this.bedTo = bedTo;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.cities = cities;
        this.city = city;
    }

    public Integer getBedFrom() {
        return bedFrom;
    }

    public void setBedFrom(Integer bedFrom) {
        this.bedFrom = bedFrom;
    }

    public Integer getBedTo() {
        return bedTo;
    }

    public void setBedTo(Integer bedTo) {
        this.bedTo = bedTo;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public BigDecimal getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(BigDecimal priceFrom) {
        this.priceFrom = priceFrom;
    }

    public BigDecimal getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(BigDecimal priceTo) {
        this.priceTo = priceTo;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "RoomFilterPto{" +
                "bedFrom=" + bedFrom +
                ", bedTo=" + bedTo +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", priceFrom=" + priceFrom +
                ", priceTo=" + priceTo +
                ", cities=" + cities +
                ", city='" + city + '\'' +
                '}';
    }
}
