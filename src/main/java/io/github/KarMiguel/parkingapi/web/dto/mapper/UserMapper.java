package io.github.KarMiguel.parkingapi.web.dto.mapper;

import io.github.KarMiguel.parkingapi.entity.Users;
import io.github.KarMiguel.parkingapi.web.dto.UserCreatedDTO;
import io.github.KarMiguel.parkingapi.web.dto.UserResponseDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static Users toUser(UserCreatedDTO userCreatedDTO){
        return  new ModelMapper().map(userCreatedDTO, Users.class);
    }


    public static UserResponseDTO toDto(Users user){
        String role =  user.getRole().name().substring("ROLE_".length());
        PropertyMap<Users,UserResponseDTO> props = new PropertyMap<Users, UserResponseDTO>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);
        return  mapper.map(user,UserResponseDTO.class);
    }

    public  static List<UserResponseDTO> toListDto(List<Users> users){
        return  users.stream().map(user -> toDto(user)).collect(Collectors.toList());
    }
}
