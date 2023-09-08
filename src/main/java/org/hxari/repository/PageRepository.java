package org.hxari.repository;

import java.util.Optional;

import org.hxari.model.PageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<PageModel, Long>
{
	Optional<PageModel> findByToken( String token );
}
