package cz.muni.pa165.bookingmanager.persistence.dao;

import cz.muni.pa165.bookingmanager.persistence.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data access layer for @{@link RoomEntity}
 * @Author Ondřej Gasior
 */
public interface RoomDao extends JpaRepository<RoomEntity, Long> {
}
