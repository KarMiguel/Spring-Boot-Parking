package io.github.KarMiguel.parkingapi.jwt;

import io.github.KarMiguel.parkingapi.entity.Users;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class JwtUserDetails extends User{

    private Users user;
    public JwtUserDetails(Users user) {
        super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().name()));
        this.user = user;
    }

    public Long getId(){
        return this.user.getId();
    }


    public String getRole(){
        return this.user.getRole().name();
    }


}
