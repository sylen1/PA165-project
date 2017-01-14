package cz.muni.pa165.bookingmanager.iface.facade;

import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;

public interface PageableFacade<T> {
    PageResult<T> findAll(PageInfo pageInfo);
}
