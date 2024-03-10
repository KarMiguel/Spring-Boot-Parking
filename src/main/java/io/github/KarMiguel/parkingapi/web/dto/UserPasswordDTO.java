package io.github.KarMiguel.parkingapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserPasswordDTO{

        @NotBlank
        @Size(min = 6,max = 6)
        private  String currentPassword;
        @NotBlank
        @Size(min = 6,max = 6)
        private String newPassword;
        @NotBlank
        @Size(min = 6,max = 6)
        private String confirmPassword;
}
