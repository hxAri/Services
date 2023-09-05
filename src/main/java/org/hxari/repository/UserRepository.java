package org.hxari.repository;

import java.util.Optional;

import org.hxari.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long>
{
	boolean existsByUsermail( String usermail );
	boolean existsByUsername( String username );
	Optional<UserModel> findByUsermail( String usermail );
	Optional<UserModel> findByUsername( String username );
}
