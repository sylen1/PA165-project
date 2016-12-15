package cz.muni.pa165.bookingmanager.web.pto;

import java.util.Date;

/**
 * Created by gasior on 16.12.2016.
 */
public class HotelStatisticFilterPto {

    private Date dateFrom;
    private Date dateTo;

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

    public HotelStatisticFilterPto(Date dateFrom, Date dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public HotelStatisticFilterPto() {
    }
}
