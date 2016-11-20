package cz.muni.pa165.bookingmanager.application.facade;

import cz.muni.pa165.bookingmanager.application.service.iface.ReservationService;
import cz.muni.pa165.bookingmanager.iface.dto.ReservationDto;
import cz.muni.pa165.bookingmanager.iface.util.ReservationFilter;
import cz.muni.pa165.bookingmanager.iface.facade.ReservationFacade;
import cz.muni.pa165.bookingmanager.iface.util.Page;
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
    private Mapper dozer;
    
    @Override
    public ReservationDto createReservation(ReservationDto reservationDto) {
        Validate.isTrue(reservationDto.getId() == null);

        ReservationEntity entity = convert(reservationDto);        
        ReservationEntity saved = reservationService.createReservation(entity);
        
        return convert(saved);
    }

    @Override
    public ReservationDto updateReservation(ReservationDto reservationDto) {
        Validate.notNull(reservationDto.getId());
        
        ReservationEntity entity = convert(reservationDto);
        ReservationEntity updated = reservationService.createReservation(entity);
        
        return convert(updated);
    }

    @Override
    public Page<ReservationDto> findAll(PageInfo pageInfo) {
        Page<ReservationEntity> pageOfEntities = reservationService.findAll(pageInfo);
        List<ReservationDto> dtos = pageOfEntities.getEntries()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
        
        return new Page<>(dtos, pageOfEntities.getPageCount(), pageOfEntities.getPageInfo());
    }

    @Override
    public Optional<ReservationDto> findById(Long id) {
        return reservationService.findById(id).map(this::convert);
    }

    @Override
    public Page<ReservationDto> findFiltered(ReservationFilter filter, PageInfo pageInfo) {
        Page<ReservationEntity> pageOfEntities = reservationService.findFiltered(filter, pageInfo);
        List<ReservationDto> dtos = pageOfEntities.getEntries()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
        
        return new Page<>(dtos, pageOfEntities.getPageCount(), pageOfEntities.getPageInfo());
    }
    
    
    private ReservationEntity convert(ReservationDto dto) {
        return dozer.map(dto, ReservationEntity.class);
    }
    
    private ReservationDto convert(ReservationEntity entity) {
        return dozer.map(entity, ReservationDto.class);
    }
}
