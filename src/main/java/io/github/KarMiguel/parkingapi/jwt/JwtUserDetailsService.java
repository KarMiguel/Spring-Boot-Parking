package io.github.KarMiguel.parkingapi.jwt;

import io.github.KarMiguel.parkingapi.entity.Users;
import io.github.KarMiguel.parkingapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userService.searchByName(username);
        return new JwtUserDetails(user);
    }

    public JwtToken getTokenAuthenticated(String username){
        Users.Role role = userService.searchRoleByUsername(username);
        return JwtUtils.cratedToken(username, role.name().substring("ROLE_".length()));
    }
}
