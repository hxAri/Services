package org.hxari.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.hxari.model.Task;
import org.hxari.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>
{
	
	@Query( value="SELECT * FROM tasks WHERE owner_id=:owner ORDER BY id DESC LIMIT :offset, :limit", nativeQuery=true )
	Optional<List<Task>> findAllByOwnerWithPaddingLimit(
		@Param( "owner" ) Long owner,
		@Param( "limit" ) Integer limit,
		@Param( "offset" ) Integer offset
	);

	@Query( value="SELECT * FROM tasks WHERE owner_id=:owner AND created>=:created ORDER BY created DESC LIMIT :offset, :limit", nativeQuery=true )
	Optional<List<Task>> findAllByOwnerAndCreatedOnWithPaddingLimit(
		@Param( "owner" ) Long owner,
		@Param( "created" ) Timestamp created,
		@Param( "limit" ) Integer limit,
		@Param( "offset" ) Integer offset
	);

	@Query( value="SELECT * FROM tasks WHERE owner_id=:owner AND updated>=:updated ORDER BY updated DESC LIMIT :offset, :limit", nativeQuery=true )
	Optional<List<Task>> findAllByOwnerAndUpdatedOnWithPaddingLimit(
		@Param( "owner" ) Long owner,
		@Param( "updated" ) Timestamp updated,
		@Param( "limit" ) Integer limit,
		@Param( "offset" ) Integer offset
	);

	Optional<List<Task>> findAllByOwner( User owner );

}
