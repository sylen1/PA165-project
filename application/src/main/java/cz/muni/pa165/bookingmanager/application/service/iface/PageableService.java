package cz.muni.pa165.bookingmanager.application.service.iface;

import cz.muni.pa165.bookingmanager.iface.util.Page;

public interface PageableService<T> {
    Page<T> findAll(int pageSize, int pageCount);
}