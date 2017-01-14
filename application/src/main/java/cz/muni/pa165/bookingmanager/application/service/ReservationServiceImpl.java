package cz.muni.pa165.bookingmanager.application.service;

import cz.muni.pa165.bookingmanager.application.service.iface.ReservationService;
import cz.muni.pa165.bookingmanager.iface.util.HotelStatistics;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.ReservationFilter;
import cz.muni.pa165.bookingmanager.persistence.dao.ReservationDao;
import cz.muni.pa165.bookingmanager.persistence.entity.DatabaseAccountState;
import cz.muni.pa165.bookingmanager.persistence.entity.ReservationEntity;
import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;
import org.apache.commons.lang3.Validate;
import org.dozer.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

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
        DatabaseAccountState das = reservation.getCustomer().getAccountState();
        Validate.isTrue(!das.equals(DatabaseAccountState.INACTIVE));
        Validate.isTrue(!isRoomReserved(reservation.getRoom().getId(),reservation.getStartDate(),reservation.getEndDate()));
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

    public Boolean isRoomReserved(Long roomId, Date intervalStart, Date intervalEnd) {
        return 0 < reservationDao.countByRoomIdValidInInterval(
                        roomId, new java.sql.Date(intervalStart.getTime()), new java.sql.Date(intervalEnd.getTime())
                    );
    }

    public HotelStatistics gatherHotelStatistics(Long hotelId, Date intervalStart, Date intervalEnd) {
        if(intervalEnd.before(intervalStart)) {
            throw new IllegalArgumentException("intervalEnd can not be before intervalStart");
        }
        // things to be returned
        BigDecimal revenue = BigDecimal.ZERO;
        double averageRoomUsage = 0;
        double averageReservationLength = 0;
        Map<RoomEntity, Long> usedNightsPerRoom = new HashMap<>();

        java.sql.Date intervalStartSql = new java.sql.Date(intervalStart.getTime());
        java.sql.Date intervalEndSql = new java.sql.Date(intervalEnd.getTime());

        List<ReservationEntity> reservations
                = reservationDao.findByHotelIdValidInInterval(hotelId, intervalStartSql, intervalEndSql);

        List<ReservationEntity> completedReservations = reservations
                .stream()
                .filter(x -> x.getState().equals("PAID") || x.getState().equals("ENDED"))
                .collect(Collectors.toList());
        List<ReservationEntity> cr9n2 = new ArrayList<>();
        for(ReservationEntity x: completedReservations){
            if(x.getStartDate().equals(intervalEndSql)) cr9n2.add(x);
        }
        completedReservations.removeAll(cr9n2);
        LocalDate intervalStartLocal = dateToLocalDate(intervalStart);
        LocalDate intervalEndLocal = dateToLocalDate(intervalEnd);


        long totalReservedNights = 0; // regardless of room
        for(ReservationEntity res : completedReservations) {
            long days = 0;
            LocalDate startDateLocal = dateToLocalDate(res.getStartDate());
            LocalDate endDateLocal = dateToLocalDate(res.getEndDate());

            if(res.getStartDate().after(intervalStart) && res.getEndDate().before(intervalEnd)) {
                days = ChronoUnit.DAYS.between(startDateLocal, endDateLocal);
            } else if (res.getStartDate().after(intervalStart)) {
                days = ChronoUnit.DAYS.between(startDateLocal, intervalEndLocal);
            } else if (res.getEndDate().before(intervalEnd)) {
                days = ChronoUnit.DAYS.between(intervalStartLocal, endDateLocal);
            } else {
                days = ChronoUnit.DAYS.between(intervalStartLocal, intervalEndLocal);
            }
            revenue = revenue.add(res.getRoom().getPrice().multiply(new BigDecimal(days)));

            if(usedNightsPerRoom.containsKey(res.getRoom())) {
                usedNightsPerRoom.replace(res.getRoom(), days + usedNightsPerRoom.get(res.getRoom()));
            } else {
                usedNightsPerRoom.put(res.getRoom(), days);
            }

            totalReservedNights += days;
        }
        long roomDays = 0;
        for(RoomEntity r : usedNightsPerRoom.keySet()) {
            roomDays += usedNightsPerRoom.get(r);
        }

        long totalNumOfNightsInInterval = numberOfdaysBetweenLocalDates(intervalStartLocal, intervalEndLocal) - 1;
        if(totalNumOfNightsInInterval > 0) averageRoomUsage = (1. * roomDays) / totalNumOfNightsInInterval;
        if(completedReservations.size() > 0) averageReservationLength = (1. * totalReservedNights) / completedReservations.size();

        return new HotelStatistics(completedReservations.size(), revenue, averageRoomUsage, averageReservationLength);
    }

    private long numberOfdaysBetweenLocalDates(LocalDate intervalStartLocal, LocalDate intervalEndLocal) {
        if(intervalEndLocal.isBefore(intervalStartLocal)) return 0;
        Chronology chronology = intervalStartLocal.getChronology();
        long totalNumOfDaysInInterval = 1;
        if(intervalEndLocal.getYear() - intervalStartLocal.getYear() == 0) {
            totalNumOfDaysInInterval += intervalEndLocal.getDayOfYear() - intervalStartLocal.getDayOfYear();
        } else if(intervalEndLocal.getYear() - intervalStartLocal.getYear() > 0) {
            totalNumOfDaysInInterval += intervalEndLocal.getDayOfYear();
            if(chronology.isLeapYear(intervalStartLocal.getYear())) totalNumOfDaysInInterval += 366;
            else totalNumOfDaysInInterval += 365;
            totalNumOfDaysInInterval -= intervalStartLocal.getDayOfYear();
            if(intervalEndLocal.getYear() - intervalStartLocal.getYear() > 1) {
                for(int y = intervalStartLocal.getYear(); y <= intervalEndLocal.getYear(); y++) {
                    if(chronology.isLeapYear(y)) totalNumOfDaysInInterval += 366;
                    else totalNumOfDaysInInterval += 365;
                }
            }
        }
        return totalNumOfDaysInInterval;
    }

    private LocalDate dateToLocalDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // In Calendar January is 0, but for LocalDate January is 1
        return LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE));
    }

    private PageResult<ReservationEntity> mapPageToPageResult(Page<ReservationEntity> page){
        PageResult<ReservationEntity> pageResult = new PageResult<>();

        mapper.map(page, pageResult);
        pageResult.setEntries(page.getContent());

        return pageResult;
    }
}
