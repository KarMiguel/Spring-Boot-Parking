package io.github.KarMiguel.parkingapi.service;

import io.github.KarMiguel.parkingapi.entity.User;
import io.github.KarMiguel.parkingapi.exception.EntityUserNotFoundException;
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
    public User save(User user) throws UsernameUniqueViolationException {

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);

        }catch (DataIntegrityViolationException ex){
            throw new UsernameUniqueViolationException(String.format("Username '%s' ja Cadastrado!",user.getUsername()));
        }

    }

    @Transactional(readOnly = true)
    public User searchById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(
                        ()-> new EntityUserNotFoundException("Usuário não encontrado."));
    }
    @Transactional
    public User updatePassword(Long id, String currentPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw  new PasswordInvalidException("Nova senha nao confere com confirmação de senha.");
        }

        User user = searchById(id);
        if (!passwordEncoder.matches(currentPassword,user.getPassword())){
            throw  new PasswordInvalidException("Sua senha não confere.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return user;
    }

    @Transactional(readOnly = true)
    public List<User> listAll() {
       return userRepository.findAll();
    }
    @Transactional(readOnly = true)
    public User searchByName(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                ()-> new EntityUserNotFoundException(String.format("Usuário com '%s' não encontrado.",username)));
    }

    @Transactional(readOnly = true)
    public User.Role searchRoleByUsername(String username) {
        return userRepository.findRoleByUsername(username);
    }
}
