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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageInfo)) return false;

        PageInfo pageInfo = (PageInfo) o;

        if (getPageNumber() != pageInfo.getPageNumber()) return false;
        return getPageSize() == pageInfo.getPageSize();

    }

    @Override
    public int hashCode() {
        int result = getPageNumber();
        result = 31 * result + getPageSize();
        return result;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                '}';
    }
}
