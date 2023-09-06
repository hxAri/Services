package org.hxari.repository;

import java.util.Optional;

import org.hxari.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
	Optional<UserModel> findByUsername( String username );
}
