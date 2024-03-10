package io.github.KarMiguel.parkingapi.repository;

import io.github.KarMiguel.parkingapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User,Long> {

}
