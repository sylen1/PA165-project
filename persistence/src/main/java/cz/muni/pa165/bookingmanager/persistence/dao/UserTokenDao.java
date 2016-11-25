package cz.muni.pa165.bookingmanager.persistence.dao;

import cz.muni.pa165.bookingmanager.persistence.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokenDao extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByEmail(String email);
}
