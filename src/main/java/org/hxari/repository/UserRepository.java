package org.hxari.repository;

import java.util.Optional;

import org.hxari.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
	boolean existsByUsermail( String usermail );
	boolean existsByUsername( String username );
	Optional<User> findByUsermail( String usermail );
	Optional<User> findByUsername( String username );
}
