package org.hxari.repository;

import java.util.Optional;

import org.hxari.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<Page, Long>
{
	Optional<Page> findByToken( String token );
}
