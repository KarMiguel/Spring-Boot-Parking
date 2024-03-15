package io.github.KarMiguel.parkingapi.web.dto.users;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class UserResponseDTO{
    private Long id;
    private String username;
    private String role;
}
