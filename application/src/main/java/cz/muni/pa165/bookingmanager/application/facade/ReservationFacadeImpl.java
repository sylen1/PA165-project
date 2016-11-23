package cz.muni.pa165.bookingmanager.application.facade;

import cz.muni.pa165.bookingmanager.application.service.iface.ReservationService;
import cz.muni.pa165.bookingmanager.iface.dto.ReservationDto;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.ReservationFilter;
import cz.muni.pa165.bookingmanager.iface.facade.ReservationFacade;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.persistence.entity.ReservationEntity;
import java.util.List;
import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Validate;
import org.dozer.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implemetation of ReservationFacade interface
 * @author Mojmír Odehnal, 374422
 */
@Transactional
@Service
public class ReservationFacadeImpl implements ReservationFacade {
    @Inject
    private ReservationService reservationService;
    
    @Inject
    private Mapper mapper;
    
    @Override
    @Transactional
    public ReservationDto createReservation(ReservationDto reservationDto) {
        Validate.isTrue(reservationDto.getId() == null);

        ReservationEntity entity = convert(reservationDto);        
        ReservationEntity saved = reservationService.createReservation(entity);
        
        return convert(saved);
    }

    @Override
    @Transactional
    public ReservationDto updateReservation(ReservationDto reservationDto) {
        Validate.notNull(reservationDto.getId());
        
        ReservationEntity entity = convert(reservationDto);
        ReservationEntity updated = reservationService.updateReservation(entity);

        return convert(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<ReservationDto> findAll(PageInfo pageInfo) {
        PageResult<ReservationEntity> pageOfEntities = reservationService.findAll(pageInfo);
        List<ReservationDto> dtos = pageOfEntities.getEntries()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
        
//        return new PageResult<>(dtos, pageOfEntities.getPageCount(), pageOfEntities.getPageInfo());
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReservationDto> findById(Long id) {
        return reservationService.findById(id).map(this::convert);
    }

    @Override
    public PageResult<ReservationDto> findFiltered(ReservationFilter filter, PageInfo pageInfo) {
        PageResult<ReservationEntity> pageOfEntities = reservationService.findFiltered(filter, pageInfo);
        List<ReservationDto> dtos = pageOfEntities.getEntries()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
        
//        return new PageResult<>(dtos, pageOfEntities.getPageCount(), pageOfEntities.getPageInfo());
        return null;
    }
    
    
    private ReservationEntity convert(ReservationDto dto) {
        return mapper.map(dto, ReservationEntity.class);
    }
    
    private ReservationDto convert(ReservationEntity entity) {
        return mapper.map(entity, ReservationDto.class);
    }
}
