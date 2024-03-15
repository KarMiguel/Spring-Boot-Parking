package io.github.KarMiguel.parkingapi.repository;

import io.github.KarMiguel.parkingapi.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<Users,Long> {

    Optional<Users> findByUsername(String username);


    @Query("Select u.role from Users u where u.username like :username")
    Users.Role findRoleByUsername(String username);
}
