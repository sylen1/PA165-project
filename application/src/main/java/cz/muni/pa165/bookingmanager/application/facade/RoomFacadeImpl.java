package cz.muni.pa165.bookingmanager.application.facade;


import cz.muni.pa165.bookingmanager.application.service.iface.RoomService;
import cz.muni.pa165.bookingmanager.iface.dto.RoomDto;
import cz.muni.pa165.bookingmanager.iface.facade.RoomFacade;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.RoomFilter;
import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;
import org.dozer.Mapper;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoomFacadeImpl implements RoomFacade {

    private RoomService roomService;
    private Mapper mapper;

    @Inject
    public RoomFacadeImpl(RoomService roomService, Mapper mapper) {
        this.roomService = roomService;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public PageResult<RoomDto> findAll(PageInfo pageInfo) {
        return convertAll(roomService.findAll(pageInfo));
    }

    @Transactional
    public RoomDto registerRoom(RoomDto roomDto) {

        RoomEntity entity = mapper.map(roomDto, RoomEntity.class);
        RoomEntity saved = roomService.registerRoom(entity);

        return mapper.map(saved, RoomDto.class);
    }

    @Transactional
    public RoomDto updateRoom(RoomDto roomDto) {

        RoomEntity entity = mapper.map(roomDto, RoomEntity.class);
        RoomEntity updated = roomService.updateRoom(entity);

        return mapper.map(updated, RoomDto.class);
    }

    @Transactional(readOnly = true)
    public Optional<RoomDto> findByName(String name) {
        return roomService.findByName(name)
                .map(x -> mapper.map(x, RoomDto.class));
    }

    @Transactional(readOnly = true)
    public Optional<RoomDto> findById(Long id)
    {
       return roomService.findById(id)
               .map(x -> mapper.map(x, RoomDto.class));
    }

    public PageResult<RoomDto> filterRooms(RoomFilter filter, PageInfo pageInfo) {
        return convertAll(roomService.filterRooms(filter, pageInfo));
    }

    private PageResult<RoomDto> convertAll(PageResult<RoomEntity> all) {

        List<RoomDto> dtos = all.getEntries()
                .stream()
                .map(x -> mapper.map(x, RoomDto.class))
                .collect(Collectors.toList());

        PageResult<RoomDto> result = new PageResult<>();
        mapper.map(all, result);
        result.setEntries(dtos);

        return result;
    }

}
