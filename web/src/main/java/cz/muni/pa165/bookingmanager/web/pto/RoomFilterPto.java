package cz.muni.pa165.bookingmanager.web.pto;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by gasior on 16.12.2016.
 */
public class RoomFilterPto {

    private int bedFrom;
    private int bedTo;
    private Date dateFrom;
    private Date dateTo;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private int currentPage;
    private int pageCount;
    private List<String> cities;
    private String city;

    public RoomFilterPto() {
    }

    public RoomFilterPto(int bedFrom, int bedTo, Date dateFrom, Date dateTo, BigDecimal priceFrom, BigDecimal priceTo, int currentPage, int pageCount, List<String> cities, String city) {
        this.bedFrom = bedFrom;
        this.bedTo = bedTo;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.currentPage = currentPage;
        this.pageCount = pageCount;
        this.cities = cities;
        this.city = city;
    }

    public int getBedFrom() {
        return bedFrom;
    }

    public void setBedFrom(int bedFrom) {
        this.bedFrom = bedFrom;
    }

    public int getBedTo() {
        return bedTo;
    }

    public void setBedTo(int bedTo) {
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

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
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
}
