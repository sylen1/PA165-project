package cz.muni.pa165.bookingmanager.iface.facade;

import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;

public interface PageableFacade<T> {
    PageResult<T> findAll(PageInfo pageInfo);
}
