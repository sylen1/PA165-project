package cz.muni.pa165.bookingmanager.iface.util;

public class PageInfo {
    private int pageNumber;
    private int pageSize;

    public PageInfo(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public PageInfo nextPageInfo(){
        return new PageInfo(pageNumber + 1, pageSize);
    }
}
