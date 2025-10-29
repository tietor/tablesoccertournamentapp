package ch.teko.tablecupbackend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.teko.tablecupbackend.model.RoleModel;
import ch.teko.tablecupbackend.service.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/roles")
public class RoleController {

  private final RoleService roleService;

  @GetMapping
  public List<RoleModel> getAllRoles() {
    return roleService.getAllRoles();
  }

}
