package io.github.KarMiguel.parkingapi.service;

import io.github.KarMiguel.parkingapi.entity.Users;
import io.github.KarMiguel.parkingapi.exception.EntityNotFoundException;
import io.github.KarMiguel.parkingapi.exception.PasswordInvalidException;
import io.github.KarMiguel.parkingapi   .exception.UsernameUniqueViolationException;
import io.github.KarMiguel.parkingapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Transactional
    public Users save(Users user) throws UsernameUniqueViolationException {

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);

        }catch (DataIntegrityViolationException ex){
            throw new UsernameUniqueViolationException(String.format("Username '%s' ja Cadastrado!",user.getUsername()));
        }

    }

    @Transactional(readOnly = true)
    public Users searchById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(
                        ()-> new EntityNotFoundException("Usuário não encontrado."));
    }
    @Transactional
    public Users updatePassword(Long id, String currentPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw  new PasswordInvalidException("Nova senha nao confere com confirmação de senha.");
        }

        Users user = searchById(id);
        if (!passwordEncoder.matches(currentPassword,user.getPassword())){
            throw  new PasswordInvalidException("Sua senha não confere.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return user;
    }

    @Transactional(readOnly = true)
    public List<Users> listAll() {
       return userRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Users searchByName(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Usuário com '%s' não encontrado.",username)));
    }

    @Transactional(readOnly = true)
    public Users.Role searchRoleByUsername(String username) {
        return userRepository.findRoleByUsername(username);
    }


}
