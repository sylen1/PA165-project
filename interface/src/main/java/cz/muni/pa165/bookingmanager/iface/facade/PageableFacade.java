package cz.muni.pa165.bookingmanager.iface.facade;

import cz.muni.pa165.bookingmanager.iface.util.Page;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;

public interface PageableFacade<T> {
    Page<T> findAll(PageInfo pageInfo);
}
