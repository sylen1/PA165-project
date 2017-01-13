package cz.muni.pa165.bookingmanager.application.facade;

import cz.muni.pa165.bookingmanager.application.service.iface.ReservationService;
import cz.muni.pa165.bookingmanager.iface.dto.ReservationDto;
import cz.muni.pa165.bookingmanager.iface.facade.ReservationFacade;
import cz.muni.pa165.bookingmanager.iface.util.HotelStatistics;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.ReservationFilter;
import cz.muni.pa165.bookingmanager.persistence.entity.ReservationEntity;
import org.apache.commons.lang3.Validate;
import org.dozer.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of ReservationFacade interface
 * @author Mojmír Odehnal, 374422
 */
@Service
public class ReservationFacadeImpl implements ReservationFacade {
    private ReservationService reservationService;
    private Mapper mapper;

    @Inject
    public ReservationFacadeImpl(ReservationService reservationService, Mapper mapper) {
        this.reservationService = reservationService;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public ReservationDto createReservation(ReservationDto reservationDto) {
        Validate.isTrue(reservationDto.getId() == null);
                
        ReservationEntity entity = mapper.map(reservationDto, ReservationEntity.class);
        ReservationEntity saved = reservationService.createReservation(entity);
        return mapper.map(saved, ReservationDto.class);
    }

    @Override
    @Transactional
    public ReservationDto updateReservation(ReservationDto reservationDto) {
        Validate.notNull(reservationDto.getId());

        ReservationEntity entity = mapper.map(reservationDto, ReservationEntity.class);
        ReservationEntity updated = reservationService.updateReservation(entity);
        return mapper.map(updated, ReservationDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<ReservationDto> findAll(PageInfo pageInfo) {
        PageResult<ReservationEntity> page = reservationService.findAll(pageInfo);
        return mapPageResultToDtos(page);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReservationDto> findById(Long id) {
        return reservationService.findById(id).map(x -> mapper.map(x, ReservationDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<ReservationDto> findFiltered(ReservationFilter filter, PageInfo pageInfo) {
        PageResult<ReservationEntity> pageOfEntities = reservationService.findFiltered(filter, pageInfo);
        return mapPageResultToDtos(pageOfEntities);
    }

    @Transactional(readOnly = true)
    public HotelStatistics gatherHotelStatistics(Long hotelId, Date intervalStart, Date intervalEnd) {
        return reservationService.gatherHotelStatistics(hotelId, intervalStart, intervalEnd);
    }

    private PageResult<ReservationDto> mapPageResultToDtos(PageResult<ReservationEntity> entityPage){
        List<ReservationDto> dtos = entityPage.getEntries()
                .stream()
                .map(x -> mapper.map(x, ReservationDto.class))
                .collect(Collectors.toList());

        PageResult<ReservationDto> result = new PageResult<>();
        mapper.map(entityPage, result);
        result.setEntries(dtos);

        return result;
    }
}
