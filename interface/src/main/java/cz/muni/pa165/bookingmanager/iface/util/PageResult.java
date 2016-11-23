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

    /**
     * @return unmodifiable list of entries
     */
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
}
