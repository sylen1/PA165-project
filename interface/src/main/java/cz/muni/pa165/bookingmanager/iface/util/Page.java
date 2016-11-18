package cz.muni.pa165.bookingmanager.iface.util;

import java.util.Collections;
import java.util.List;

public class Page<T> {
    private List<T> entries;
    private int pageCount;
    private PageInfo pageInfo;

    public Page(List<T> entries, int pageCount, PageInfo pageInfo) {
        this.entries = entries;
        this.pageCount = pageCount;
        this.pageInfo = pageInfo;
    }

    /**
     * @return unmodifiable list of entries
     */
    public List<T> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public int getPageNumber() {
        return pageInfo.getPageNumber();
    }

    public int getPageCount() {
        return pageCount;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public boolean hasNext(){
        return getPageNumber() < pageCount;
    }

    /**
     * Returns PageInfo for next page, or null if doesn't exists
     */
    public PageInfo nextPageInfo(){
        return hasNext() ? pageInfo.nextPageInfo() : null;
    }
}
