package cz.muni.pa165.bookingmanager.application.service;

import cz.muni.pa165.bookingmanager.application.service.iface.RoomService;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.RoomFilter;
import cz.muni.pa165.bookingmanager.persistence.dao.RoomDao;
import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;
import org.apache.commons.lang3.Validate;
import org.dozer.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Gasior
 */
public class RoomServiceImpl implements RoomService {

    private RoomDao roomDao;
    private Mapper mapper;

    @Inject
    public RoomServiceImpl(RoomDao roomDao, Mapper mapper) {
        this.roomDao = roomDao;
        this.mapper = mapper;
    }

    @Override
    public PageResult<RoomEntity> findAll(PageInfo pageInfo) {

        if(pageInfo == null) {
            throw  new IllegalArgumentException("pageInfo cannot be null");
        }
        Pageable pageRequest = new PageRequest(pageInfo.getPageNumber(), pageInfo.getPageSize());
        Page<RoomEntity> page = roomDao.findAll(pageRequest);
        return mapPage(page);
    }

    @Override
    public RoomEntity registerRoom(RoomEntity roomEntity) {
        Validate.isTrue(roomEntity.getId() == null);
        return roomDao.save(roomEntity);
    }

    @Override
    public RoomEntity updateRoom(RoomEntity roomEntity) {
        Validate.notNull(roomEntity.getId());
        return roomDao.save(roomEntity);
    }

    @Override
    public Optional<RoomEntity> findByName(String name) {
        Validate.notEmpty(name);
        return Optional.ofNullable(roomDao.findByName(name));
    }

    @Override
    public Optional<RoomEntity> findById(Long id) {
        Validate.notNull(id);
        return Optional.ofNullable(roomDao.findOne(id));
    }

    @Override
    public PageResult<RoomEntity> filterRooms(RoomFilter filter, PageInfo pageInfo) {

        if(filter == null || pageInfo == null) {
            throw new IllegalArgumentException("Parameters cannot be null!");
        }
        Pageable pageRequest = new PageRequest(pageInfo.getPageNumber(), pageInfo.getPageSize());
        int bedFrom;
        int bedTo;
        BigDecimal priceFrom;
        BigDecimal priceTo;
        if(filter.getBedContFrom() == null) {
            bedFrom = 0;
        } else {
            bedFrom = filter.getBedContFrom().get();
        }
        if(filter.getBedCountTo() == null) {
            bedTo = Integer.MAX_VALUE;
        } else {
            bedTo = filter.getBedCountTo().get();
        }
        if(filter.getPriceFrom() == null) {
            priceFrom = new BigDecimal(0);
        } else {
            priceFrom = filter.getPriceFrom().get();
        }
        if(filter.getBedCountTo() == null) {
            priceTo = new BigDecimal(Double.MAX_VALUE);
        } else {
            priceTo = filter.getPriceTo().get();
        }

        Page<RoomEntity> page = roomDao.findByBedCountGreaterThanAndBedCountLessThanAndPriceGreaterThanAndPriceLessThan(bedFrom, bedTo, priceFrom, priceTo, pageRequest);
        return mapPage(page);
    }

    private PageResult<RoomEntity> mapPage(Page<RoomEntity> page){
        PageResult<RoomEntity> pageResult = new PageResult<>();
        mapper.map(page, pageResult);
        pageResult.setEntries(page.getContent());

        return pageResult;
    }
}
