package cz.muni.pa165.bookingmanager.application.facade;

import cz.muni.pa165.bookingmanager.application.service.iface.HotelService;
import cz.muni.pa165.bookingmanager.iface.dto.HotelDto;
import cz.muni.pa165.bookingmanager.iface.facade.HotelFacade;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.persistence.entity.HotelEntity;
import org.apache.commons.lang3.Validate;
import org.dozer.Mapper;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HotelFacadeImpl implements HotelFacade {
    @Inject
    private HotelService hotelService;

    @Inject
    private Mapper mapper;

    @Override
    @Transactional
    public HotelDto registerHotel(HotelDto hotelDto) {
        Validate.isTrue(hotelDto.getId() == null);

        HotelEntity entity = mapper.map(hotelDto, HotelEntity.class);
        HotelEntity saved = hotelService.registerHotel(entity);

        return mapper.map(saved, HotelDto.class);
    }

    @Override
    @Transactional
    public HotelDto updateHotelInformation(HotelDto hotelDto) {
        Validate.notNull(hotelDto.getId());

        HotelEntity entity = mapper.map(hotelDto, HotelEntity.class);
        HotelEntity updated = hotelService.updateHotelInformation(entity);

        return mapper.map(updated, HotelDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<HotelDto> findByCity(String city, PageInfo pageInfo) {
        PageResult<HotelEntity> entityPage = hotelService.findByCity(city, pageInfo);
        return mapPage(entityPage);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HotelDto> findById(Long id) {
        return hotelService.findById(id)
                .map(x -> mapper.map(x, HotelDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<HotelDto> findAll(PageInfo pageInfo) {
        PageResult<HotelEntity> entityPage = hotelService.findAll(pageInfo);
        return mapPage(entityPage);
    }

    private PageResult<HotelDto> mapPage(PageResult<HotelEntity> entityPage){
        List<HotelDto> dtos = entityPage.getEntries()
                .stream()
                .map(x -> mapper.map(x, HotelDto.class))
                .collect(Collectors.toList());

        PageResult<HotelDto> result = new PageResult<>();
        mapper.map(entityPage, result);
        result.setEntries(dtos);

        return result;
    }
}
