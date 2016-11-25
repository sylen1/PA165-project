package cz.muni.pa165.bookingmanager.application.facade;

import cz.muni.pa165.bookingmanager.application.service.iface.UserService;
import cz.muni.pa165.bookingmanager.iface.dto.UserDto;
import cz.muni.pa165.bookingmanager.iface.dto.UserLoginDto;
import cz.muni.pa165.bookingmanager.iface.facade.UserFacade;
import cz.muni.pa165.bookingmanager.iface.util.PageResult;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.persistence.entity.UserEntity;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
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
    private UserService userService;
    private Mapper mapper;

    @Inject
    public UserFacadeImpl(UserService userService, Mapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

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
    public Pair<UserDto,String> registerUser(UserDto user, String passwd) {
        UserEntity entity = this.userDtoToEntity(user);
        Pair<UserEntity,String> x = userService.registerUser(entity,passwd);
        if(x == null) return null;
        user.setId(entity.getId());
        user.setPasswordHash(entity.getPasswordHash());
        user.setPasswordSalt(entity.getPasswordSalt());
        Pair<UserDto,String> rv = new ImmutablePair<>(user,x.getRight());
        return rv;
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
        PageResult<UserDto> rv = new PageResult<>();
        mapper.map(entityPage,rv);
        rv.setEntries(dtoList);
        return rv;
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto user) {
        Validate.notNull(user.getId());
        UserEntity entity = this.userDtoToEntity(user);
        UserEntity updated = userService.updateUser(entity);
        return this.userEntityToDto(updated);
    }

    @Override
    public boolean confirmUserRegistration(String email, String token) {
        Optional<UserEntity> a = userService.findByEmail(email);
        if(!a.isPresent()) return false;
        String cmp = Integer.toHexString(a.get().hashCode()).toLowerCase();
        for(int i=0;i<16384;i++){
            cmp = Integer.toHexString(cmp.hashCode()).toLowerCase();
        }
        cmp = cmp.toLowerCase();
        return cmp.equals(token);
    }

    private UserEntity userDtoToEntity(UserDto dto){
        return mapper.map(dto,UserEntity.class);
    }
    private UserDto userEntityToDto(UserEntity entity){
        return mapper.map(entity,UserDto.class);
    }
}
