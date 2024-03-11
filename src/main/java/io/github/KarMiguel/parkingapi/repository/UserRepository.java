package io.github.KarMiguel.parkingapi.repository;

import io.github.KarMiguel.parkingapi.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Long> {

    Optional<Users> findByUsername(String username);

    @Query("Select u.Role from Users u where u.username like:username")
    Users.Role findRoleByUsername(String username);
}
