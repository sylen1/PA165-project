package cz.muni.pa165.bookingmanager.iface.util;

import java.util.LinkedList;
import java.util.List;

public class PageResult<T> {
    private List<T> entries;
    private int totalEntries;
    private int pageCount;
    private int pageNumber;
    private int pageSize;

    public PageResult() {
        entries = new LinkedList<>();
    }

    public List<T> getEntries() {
        return entries;
    }

    public void setEntries(List<T> entries) {
        this.entries = entries;
    }

    public int getTotalEntries() {
        return totalEntries;
    }

    public void setTotalEntries(int totalEntries) {
        this.totalEntries = totalEntries;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getEntrySize(){
        return entries.size();
    }

    public boolean hasNext(){
        return pageNumber + 1 < pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Returns PageInfo for next page, or null if doesn't exists
     */
    public PageInfo nextPageInfo(){
        return hasNext() ? new PageInfo(pageNumber + 1, pageSize) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PageResult<?> that = (PageResult<?>) o;

        if (getTotalEntries() != that.getTotalEntries()) return false;
        if (getPageCount() != that.getPageCount()) return false;
        if (getPageNumber() != that.getPageNumber()) return false;
        if (getPageSize() != that.getPageSize()) return false;
        return getEntries() != null ? getEntries().equals(that.getEntries()) : that.getEntries() == null;

    }

    @Override
    public int hashCode() {
        int result = getEntries() != null ? getEntries().hashCode() : 0;
        result = 31 * result + getTotalEntries();
        result = 31 * result + getPageCount();
        result = 31 * result + getPageNumber();
        result = 31 * result + getPageSize();
        return result;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "entries=" + entries +
                ", totalEntries=" + totalEntries +
                ", pageCount=" + pageCount +
                ", pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                '}';
    }
}
