package io.github.KarMiguel.parkingapi.entity;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true,length = 100)
    private String username;

    @Column(nullable = false,length = 100)
    private String password;

    @Column(nullable = false,length = 25)
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_CLIENTE;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column(name = "date_modification")
    private  LocalDateTime dateModification;

    @Column(name = "created_at")
    private String  createdAt;

    @Column(name = "modified_by")
    private  String modifiedBy;

    public enum Role{
        ROLE_ADMIN,
        ROLE_CLIENTE
    }

    @PrePersist
    public  void pre(){
      setRole(Role.ROLE_CLIENTE);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                '}';
    }
}
