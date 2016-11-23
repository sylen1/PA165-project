package cz.muni.pa165.bookingmanager.application.facade;

import cz.muni.pa165.bookingmanager.application.service.iface.HotelService;
import cz.muni.pa165.bookingmanager.iface.dto.HotelDto;
import cz.muni.pa165.bookingmanager.iface.facade.HotelFacade;
import cz.muni.pa165.bookingmanager.iface.util.Page;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.persistence.entity.HotelEntity;
import org.apache.commons.lang3.Validate;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HotelFacadeImpl implements HotelFacade {
    @Inject
    private HotelService hotelService;

    @Override
    @Transactional
    public HotelDto registerHotel(HotelDto hotelDto) {
        Validate.isTrue(hotelDto.getId() == null);

        HotelEntity entity = convert(hotelDto);
        HotelEntity saved = hotelService.registerHotel(entity);

        return convert(saved);
    }

    @Override
    @Transactional
    public HotelDto updateHotelInformation(HotelDto hotelDto) {
        Validate.notNull(hotelDto.getId());

        HotelEntity entity = convert(hotelDto);
        HotelEntity updated = hotelService.updateHotelInformation(entity);

        return convert(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HotelDto> findByCity(String city, PageInfo pageInfo) {

        Page<HotelEntity> entityPage = hotelService.findByCity(city, pageInfo);
        List<HotelDto> dtos = entityPage.getEntries()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());

        return new Page<>(dtos, entityPage.getPageCount(), entityPage.getPageInfo());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HotelDto> findById(Long id) {
        return hotelService.findById(id)
                .map(this::convert);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HotelDto> findAll(PageInfo pageInfo) {
        Page<HotelEntity> entityPage = hotelService.findAll(pageInfo);
        List<HotelDto> dtos = entityPage.getEntries()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());

        return new Page<>(dtos, entityPage.getPageCount(), entityPage.getPageInfo());
    }

    // TODO mapping using dozer
    private HotelEntity convert(HotelDto dto){
        return null;
    }

    private HotelDto convert(HotelEntity entity){
        return null;
    }

}
