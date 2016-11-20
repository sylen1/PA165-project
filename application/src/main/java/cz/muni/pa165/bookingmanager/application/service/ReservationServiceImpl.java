package cz.muni.pa165.bookingmanager.application.service;

import cz.muni.pa165.bookingmanager.application.service.iface.ReservationService;
import cz.muni.pa165.bookingmanager.iface.util.Page;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.ReservationFilter;
import cz.muni.pa165.bookingmanager.persistence.dao.ReservationDao;
import cz.muni.pa165.bookingmanager.persistence.entity.HotelEntity;
import cz.muni.pa165.bookingmanager.persistence.entity.ReservationEntity;
import static java.lang.Math.toIntExact;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Inject
    private ReservationDao reservationDao;
    
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
    public Page<ReservationEntity> findAll(PageInfo pageInfo) {
        Pageable pageRequest = new PageRequest(pageInfo.getPageNumber(), pageInfo.getPageSize());

        List<ReservationEntity> entities = reservationDao.findAll(pageRequest).getContent();
        int countAll = toIntExact(reservationDao.count());

        return new Page<>(entities, countAll, pageInfo);
    }

    @Override
    public Optional<ReservationEntity> findById(Long id) {
        return Optional.ofNullable(reservationDao.findOne(id));
    }

    @Override
    public Page<ReservationEntity> findFiltered(ReservationFilter filter, PageInfo pageInfo) {
        Pageable pageRequest = new PageRequest(pageInfo.getPageNumber(), pageInfo.getPageSize());
        
        Long roomId = filter.getRoomId().orElse(null);
        Long customerId = filter.getCustomerId().orElse(null);
        java.sql.Date startsBefore = filter.getStartsBefore()
                .map(d -> new java.sql.Date(d.getTime())).orElse(null);
        java.sql.Date endsAfter = filter.getEndsAfter()
                .map(d -> new java.sql.Date(d.getTime())).orElse(null);
        String state = filter.getState().map(s -> s.name()).orElse(null);
        
        List<ReservationEntity> entities = reservationDao.findByOptionalCustomCriteria(
                roomId, customerId, startsBefore, endsAfter, state, pageRequest);
        int count = toIntExact(reservationDao.countByOptionalCustomCriteria(
                roomId, customerId, startsBefore, endsAfter, state));

        return new Page<>(entities, count, pageInfo);
    }
}
