package cz.muni.pa165.bookingmanager.application.facade;


import cz.muni.pa165.bookingmanager.iface.dto.RoomDto;
import cz.muni.pa165.bookingmanager.iface.util.RoomFilter;
import cz.muni.pa165.bookingmanager.application.service.iface.RoomService;
import cz.muni.pa165.bookingmanager.iface.facade.RoomFacade;
import cz.muni.pa165.bookingmanager.iface.util.Page;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;

import javax.inject.Inject;
import java.util.Optional;

public class RoomFacadeImpl implements RoomFacade {

    @Inject
    private RoomService service;

    public Page<RoomDto> findAll(PageInfo pageInfo) {
        return ConvertAll(service.findAll(pageInfo));
    }

    public RoomDto registerRoom(RoomDto roomDto) {
        return ConvertOne(service.registerRoom(ConvertOneBack(roomDto)));
    }

    public RoomDto updateRoom(RoomDto roomDto) {
        return ConvertOne(service.updateRoom(ConvertOneBack(roomDto)));
    }

    public Optional<RoomDto> findByName(String name) {
        return ConvertOneOptional(service.findByName(name));
    }

    public Optional<RoomDto> findById(Long id) {
       return ConvertOneOptional(service.findById(id));
    }

    public Page<RoomDto> filterRooms(RoomFilter filter, PageInfo pageInfo) {
        return ConvertAll(service.filterRooms(filter, pageInfo));
    }

    private Page<RoomDto> ConvertAll(Page<RoomEntity> all) {
        return null; //TODO
    }
    private RoomEntity ConvertOneBack(RoomDto roomDto) {
        return null; // TODO
    }
    private RoomDto ConvertOne(RoomEntity roomEntity) {
        return null; // TODO
    }

    private Optional<RoomDto> ConvertOneOptional(Optional<RoomEntity> byName) {
        return null; // TODO
    }

}
