package org.hxari.repository;

import java.util.Optional;

import org.hxari.model.RoleModel;
import org.hxari.model.RoleModel.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Long> {
	Optional<RoleModel> findByRole( Role role );
}
