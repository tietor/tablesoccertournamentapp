package ch.teko.tablecupbackend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ch.teko.tablecupbackend.model.RoleModel;
import ch.teko.tablecupbackend.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

  private final RoleRepository roleRepository;

  public List<RoleModel> getAllRoles() {
    List<RoleModel> roles = new ArrayList<>();
    roleRepository.findAll()
        .forEach(role -> roles.add(new RoleModel(role.getInternalName().name(), role.getDisplayName())));
    return roles;
  }


}
