package cz.muni.pa165.bookingmanager.iface.facade;

import cz.muni.pa165.bookingmanager.iface.util.Page;

public interface PageableFacade<T> {
    Page<T> findAll(int pageSize, int pageCount);
}
