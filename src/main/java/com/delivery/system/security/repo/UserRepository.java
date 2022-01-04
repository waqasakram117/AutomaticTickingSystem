package com.delivery.system.security.repo;

import com.delivery.system.security.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByUserName(String username);
	boolean existsByUserNameAndRole(String username, String roles);
}
