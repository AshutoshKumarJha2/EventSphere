package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository for User entity
 * * @author 2480010
 *
 * @version 1.0
 * @since 28-02-2026
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
