package io.github.KarMiguel.parkingapi.web.dto.mapper;

import io.github.KarMiguel.parkingapi.entity.User;
import io.github.KarMiguel.parkingapi.web.dto.UserCreatedDTO;
import io.github.KarMiguel.parkingapi.web.dto.UserResponseDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toUser(UserCreatedDTO userCreatedDTO){
        return  new ModelMapper().map(userCreatedDTO,User.class);
    }


    public static UserResponseDTO toDto(User user){
        String role =  user.getRole().name().substring("ROLE_".length());
        PropertyMap<User,UserResponseDTO> props = new PropertyMap<User, UserResponseDTO>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);
        return  mapper.map(user,UserResponseDTO.class);
    }

    public  static List<UserResponseDTO> toListDto(List<User> users){
        return  users.stream().map(user -> toDto(user)).collect(Collectors.toList());
    }
}
