package cz.muni.pa165.bookingmanager.application.facade;

import cz.muni.pa165.bookingmanager.application.service.iface.UserService;
import cz.muni.pa165.bookingmanager.iface.dto.UserDto;
import cz.muni.pa165.bookingmanager.iface.dto.UserLoginDto;
import cz.muni.pa165.bookingmanager.iface.facade.UserFacade;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.persistence.entity.UserEntity;
import org.apache.commons.lang3.Validate;
import org.dozer.Mapper;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * UserFacade implementation
 * @author Matej Harcar, 422714
 */
public class UserFacadeImpl implements UserFacade{

    @Inject
    private UserService userService;

    @Inject
    private Mapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findByEmail(String email) {
        return userService.findByEmail(email).map(this::userEntityToDto);
    }

    @Override
    @Transactional
    public boolean authenticate(UserLoginDto u) {
        boolean result = false;
        Optional<UserEntity> byEmail = userService.findByEmail(u.getEmail());
        if (byEmail.isPresent()){
            result = userService.authenticate(byEmail.get(), u.getPasswd());
        }
        return result;
    }

    @Override
    @Transactional
    public boolean registerUser(UserDto user, String passwd) {
        UserEntity entity = this.userDtoToEntity(user);
        if(!(userService.registerUser(entity,passwd))) return false;
        user.setId(entity.getId());
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAdmin(UserDto u) {
        return userService.isAdmin(mapper.map(u,UserEntity.class));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<UserDto> findAll(PageInfo pageInfo) {
        PageResult<UserEntity> entityPage = userService.findAll(pageInfo);
        List<UserDto> dtoList = entityPage.getEntries()
                .stream().map(this::userEntityToDto)
                .collect(Collectors.toList());
//        return new PageResult<>(dtoList,entityPage.getPageCount()
//                ,entityPage.getPageInfo());
        return null;
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto user) {
        Validate.notNull(user.getId());
        UserEntity entity = this.userDtoToEntity(user);
        UserEntity updated = userService.updateUser(entity);
        return this.userEntityToDto(updated);
    }

    private UserEntity userDtoToEntity(UserDto dto){
        return mapper.map(dto,UserEntity.class);
    }
    private UserDto userEntityToDto(UserEntity entity){
        return mapper.map(entity,UserDto.class);
    }
}
