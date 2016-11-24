package cz.muni.pa165.bookingmanager.application.service;

import cz.muni.pa165.bookingmanager.application.service.iface.ReservationService;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.ReservationFilter;
import cz.muni.pa165.bookingmanager.persistence.dao.ReservationDao;
import cz.muni.pa165.bookingmanager.persistence.entity.ReservationEntity;
import org.apache.commons.lang3.Validate;
import org.dozer.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Implementation of ReservationService interface
 * @author Mojmír Odehnal, 374422
 */
@Service
public class ReservationServiceImpl implements ReservationService {
    private ReservationDao reservationDao;
    private Mapper mapper;

    @Inject
    public ReservationServiceImpl(ReservationDao reservationDao, Mapper mapper) {
        this.reservationDao = reservationDao;
        this.mapper = mapper;
    }

    @Override
    public ReservationEntity createReservation(ReservationEntity reservation) {
        Validate.isTrue(reservation.getId() == null);
        return reservationDao.save(reservation);
    }

    @Override
    public ReservationEntity updateReservation(ReservationEntity reservation) {
        Validate.notNull(reservation.getId());
        return reservationDao.save(reservation);
    }

    @Override
    public PageResult<ReservationEntity> findAll(PageInfo pageInfo) {
        Pageable pageRequest = new PageRequest(pageInfo.getPageNumber(), pageInfo.getPageSize());

        Page page = reservationDao.findAll(pageRequest);

        return mapPageToPageResult(page);
    }

    @Override
    public Optional<ReservationEntity> findById(Long id) {
        return Optional.ofNullable(reservationDao.findOne(id));
    }

    @Override
    public PageResult<ReservationEntity> findFiltered(ReservationFilter filter, PageInfo pageInfo) {
        Pageable pageRequest = new PageRequest(pageInfo.getPageNumber(), pageInfo.getPageSize());

        Long roomId = filter.getRoomId().orElse(null);
        Long customerId = filter.getCustomerId().orElse(null);
        java.sql.Date startsBefore = filter.getStartsBefore()
                .map(d -> new java.sql.Date(d.getTime())).orElse(null);
        java.sql.Date endsAfter = filter.getEndsAfter()
                .map(d -> new java.sql.Date(d.getTime())).orElse(null);
        String state = filter.getState().map(s -> s.name()).orElse(null);

        Page<ReservationEntity> page = reservationDao.findByOptionalCustomCriteria(
                roomId, customerId, startsBefore, endsAfter, state, pageRequest);

        return mapPageToPageResult(page);
    }

    private PageResult<ReservationEntity> mapPageToPageResult(Page<ReservationEntity> page){
        PageResult<ReservationEntity> pageResult = new PageResult<>();

        mapper.map(page, pageResult);
        pageResult.setEntries(page.getContent());

        return pageResult;
    }
}
