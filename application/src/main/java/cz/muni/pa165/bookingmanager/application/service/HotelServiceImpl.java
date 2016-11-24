package cz.muni.pa165.bookingmanager.application.service;

import cz.muni.pa165.bookingmanager.application.service.iface.HotelService;
import cz.muni.pa165.bookingmanager.iface.dto.HotelDto;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.persistence.dao.HotelDao;
import cz.muni.pa165.bookingmanager.persistence.entity.HotelEntity;
import org.apache.commons.lang3.Validate;
import org.dozer.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.ManyToOne;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Math.toIntExact;

@Service
public class HotelServiceImpl implements HotelService {
    private HotelDao hotelDao;
    private Mapper mapper;

    @Inject
    public HotelServiceImpl(HotelDao hotelDao, Mapper mapper) {
        this.hotelDao = hotelDao;
        this.mapper = mapper;
    }

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
        Page<HotelEntity> page = hotelDao.findByCity(city, pageRequest);
        return mapPage(page);
    }
    @Override
    public Optional<HotelEntity> findById(Long id) {
        return Optional.ofNullable(hotelDao.findOne(id));
    }

    @Override
    public PageResult<HotelEntity> findAll(PageInfo pageInfo) {
        Pageable pageRequest = new PageRequest(pageInfo.getPageNumber(), pageInfo.getPageSize());
        Page<HotelEntity> page = hotelDao.findAll(pageRequest);
        return mapPage(page);
    }

    private PageResult<HotelEntity> mapPage(Page<HotelEntity> page){
        PageResult<HotelEntity> pageResult = new PageResult<>();
        mapper.map(page, pageResult);
        pageResult.setEntries(page.getContent());

        return pageResult;
    }
}
