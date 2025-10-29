package ch.teko.tablecupbackend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ch.teko.tablecupbackend.constant.UserRole;
import ch.teko.tablecupbackend.dto.RegisterDTO;
import ch.teko.tablecupbackend.entity.Role;
import ch.teko.tablecupbackend.entity.User;
import ch.teko.tablecupbackend.exception.RoleNotExistsException;
import ch.teko.tablecupbackend.exception.UsernameAlreadyExistsException;
import ch.teko.tablecupbackend.repository.RoleRepository;
import ch.teko.tablecupbackend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public ResponseEntity<Void> registerUser(RegisterDTO registerDTO) {
    String username = registerDTO.getUsername();
    userRepository.findByUsername(username).ifPresent(user -> {
      throw new UsernameAlreadyExistsException(user.getUsername());
    });
    UserRole selectedRole;
    try {
      selectedRole = UserRole.valueOf(registerDTO.getRole());
    } catch (IllegalArgumentException exception) {
      throw new RoleNotExistsException("Diese Rolle existiert nicht!");
    }

    Role role = roleRepository.findRoleByInternalName(selectedRole)
        .orElseThrow(() -> new RoleNotExistsException("Ausgewählte Rolle existiert nicht mehr!"));

    userRepository.save(new User(username, bCryptPasswordEncoder.encode(registerDTO.getPassword()), role));
    return ResponseEntity.ok().build();
  }

}
