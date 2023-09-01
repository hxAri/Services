package org.hxari.repository;

import java.util.List;
import java.util.Optional;

import org.hxari.model.Token;
import org.hxari.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long>
{
	Optional<Token> findByToken( String token );
	Optional<Token> findByUuids( String uuids );
	Optional<List<Token>> findByUser( User user );
}
