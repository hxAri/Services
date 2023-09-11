package org.hxari.repository;

import java.sql.Timestamp;

import org.hxari.model.TaskModel;
import org.hxari.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, Long> {
	Long countByOwnerAndCreatedAfter( UserModel owner, Timestamp created );
	Long countByOwnerAndCreatedAfterAndCreatedBefore( UserModel owner, Timestamp created, Timestamp before );
	Long countByOwnerAndUpdatedAfter( UserModel owner, Timestamp updated );
	Long countByOwnerAndUpdatedAfterAndUpdatedBefore( UserModel owner, Timestamp updated, Timestamp before );
	Long countByOwner( UserModel owner );
}
