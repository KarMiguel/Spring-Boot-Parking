package io.github.KarMiguel.parkingapi.rest.dto.users;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class UserResponseDTO{
    private Long id;
    private String username;
    private String role;
}
