package cz.muni.pa165.bookingmanager.application.service;

import cz.muni.pa165.bookingmanager.application.service.iface.HotelService;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.persistence.dao.HotelDao;
import cz.muni.pa165.bookingmanager.persistence.entity.HotelEntity;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.toIntExact;

@Service
public class HotelServiceImpl implements HotelService {
    @Inject
    private HotelDao hotelDao;

    @Override
    public HotelEntity registerHotel(HotelEntity hotelEntity) {
        Validate.isTrue(hotelEntity.getId() == null);
        return hotelDao.save(hotelEntity);
    }

    @Override
    public HotelEntity updateHotelInformation(HotelEntity hotelEntity) {
        Validate.notNull(hotelEntity.getId());
        return hotelDao.save(hotelEntity);
    }

    @Override
    public PageResult<HotelEntity> findByCity(String city, PageInfo pageInfo) {
        Pageable pageRequest = new PageRequest(pageInfo.getPageNumber(), pageInfo.getPageSize());

        List<HotelEntity> byCity = hotelDao.findByCity(city, pageRequest);
        int countByCity = hotelDao.countByCity(city);

//        return new PageResult<>(byCity, countByCity, pageInfo);
        return null;
    }
    @Override
    public Optional<HotelEntity> findById(Long id) {
        return Optional.ofNullable(hotelDao.findOne(id));
    }

    @Override
    public PageResult<HotelEntity> findAll(PageInfo pageInfo) {
        Pageable pageRequest = new PageRequest(pageInfo.getPageNumber(), pageInfo.getPageSize());

        List<HotelEntity> entities = hotelDao.findAll(pageRequest).getContent();
        int countAll = toIntExact(hotelDao.count());

//        return new PageResult<>(entities, countAll, pageInfo);
        return null;
    }
}
