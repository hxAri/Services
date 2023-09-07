package org.hxari.repository;

import org.hxari.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Long> {
	RoleModel findByRole( String role );
}
