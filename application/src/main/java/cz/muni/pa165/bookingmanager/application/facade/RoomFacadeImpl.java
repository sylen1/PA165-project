package cz.muni.pa165.bookingmanager.application.facade;


import cz.muni.pa165.bookingmanager.iface.dto.RoomDto;
import cz.muni.pa165.bookingmanager.iface.util.RoomFilter;
import cz.muni.pa165.bookingmanager.application.service.iface.RoomService;
import cz.muni.pa165.bookingmanager.iface.facade.RoomFacade;
import cz.muni.pa165.bookingmanager.iface.util.Page;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Optional;

public class RoomFacadeImpl implements RoomFacade {

    @Inject
    private RoomService service;

    @Transactional(readOnly = true)
    public Page<RoomDto> findAll(PageInfo pageInfo) {
        return convertAll(service.findAll(pageInfo));
    }

    @Transactional
    public RoomDto registerRoom(RoomDto roomDto) {
        return convertOne(service.registerRoom(convertOneBack(roomDto)));
    }

    @Transactional
    public RoomDto updateRoom(RoomDto roomDto) {
        return convertOne(service.updateRoom(convertOneBack(roomDto)));
    }

    @Transactional(readOnly = true)
    public Optional<RoomDto> findByName(String name) {
        return service.findByName(name).map(this::convertOne);
    }

    @Transactional(readOnly = true)
    public Optional<RoomDto> findById(Long id) {
       return service.findById(id).map(this::convertOne);
    }

    public Page<RoomDto> filterRooms(RoomFilter filter, PageInfo pageInfo) {
        return convertAll(service.filterRooms(filter, pageInfo));
    }

    private Page<RoomDto> convertAll(Page<RoomEntity> all) {
        return null; //TODO
    }
    private RoomEntity convertOneBack(RoomDto roomDto) {
        return null; // TODO
    }
    private RoomDto convertOne(RoomEntity roomEntity) {
        return null; // TODO
    }

}
