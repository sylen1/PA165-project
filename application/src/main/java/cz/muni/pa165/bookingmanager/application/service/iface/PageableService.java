package cz.muni.pa165.bookingmanager.application.service.iface;

import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;

public interface PageableService<T> {
    PageResult<T> findAll(PageInfo pageInfo);
}