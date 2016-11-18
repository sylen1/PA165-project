package cz.muni.pa165.bookingmanager.application.service.iface;

import cz.muni.pa165.bookingmanager.iface.util.Page;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;

public interface PageableService<T> {
    Page<T> findAll(PageInfo pageInfo);
}