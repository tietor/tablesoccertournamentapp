package ch.teko.tablecupbackend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ch.teko.tablecupbackend.constant.UserRole;
import ch.teko.tablecupbackend.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

  Optional<Role> findRoleByInternalName(UserRole internalRoleName);

}
