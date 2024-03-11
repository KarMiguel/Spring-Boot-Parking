package io.github.KarMiguel.parkingapi.repository;

import io.github.KarMiguel.parkingapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);

    @Query("Select u.Role from User u where u.username like:username")
    User.Role findRoleByUsername(String username);
}
