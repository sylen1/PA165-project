package cz.muni.pa165.bookingmanager.application.facade;

import cz.muni.pa165.bookingmanager.application.service.iface.UserService;
import cz.muni.pa165.bookingmanager.iface.dto.UserDto;
import cz.muni.pa165.bookingmanager.iface.dto.UserLoginDto;
import cz.muni.pa165.bookingmanager.iface.facade.UserFacade;
import cz.muni.pa165.bookingmanager.iface.util.Page;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.persistence.entity.UserEntity;
import org.apache.commons.lang3.Validate;
import org.dozer.Mapper;

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
    private UserService usersvc;

    @Inject
    private Mapper mapper;

    @Override
    public Optional<UserDto> findByEmail(String email) {
        Optional<UserEntity> entityOptional = usersvc.findByEmail(email);
        return Optional.of(this.userEntityToDto(entityOptional.get()));
    }

    @Override
    public boolean authenticate(UserLoginDto u) {
        return usersvc.authenticate
                (usersvc.findByEmail(u.getEmail()),u.getPasswd());
    }

    @Override
    public boolean registerUser(UserDto user, String passwd) {
        UserEntity entity = this.userDtoToEntity(user);
        if(!(usersvc.registerUser(entity,passwd))) return false;
        user.setId(entity.getId());
        return true;
    }

    @Override
    public boolean isAdmin(UserDto u) {
        return usersvc.isAdmin(mapper.map(u,UserEntity.class));
    }

    @Override
    public Page<UserDto> findAll(PageInfo pageInfo) {
        Page<UserEntity> entityPage = usersvc.findAll(pageInfo);
        List<UserDto> dtoList = entityPage.getEntries()
                .stream().map(this::userEntityToDto)
                .collect(Collectors.toList());
        return new Page<>(dtoList,entityPage.getPageCount()
                ,entityPage.getPageInfo());
    }

    @Override
    public UserDto updateUser(UserDto user) {
        Validate.notNull(user.getId());
        UserEntity entity = this.userDtoToEntity(user);
        UserEntity updated = usersvc.updateUser(entity);
        return this.userEntityToDto(updated);
    }

    private UserEntity userDtoToEntity(UserDto dto){
        return mapper.map(dto,UserEntity.class);
    }
    private UserDto userEntityToDto(UserEntity entity){
        return mapper.map(entity,UserDto.class);
    }
}
