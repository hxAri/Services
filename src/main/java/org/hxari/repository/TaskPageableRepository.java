package org.hxari.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.hxari.model.TaskModel;
import org.hxari.model.UserModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskPageableRepository extends PagingAndSortingRepository<TaskModel, Long> {
	Optional<List<TaskModel>> findAllByOwnerAndCreatedBefore( UserModel owner, Timestamp created, Pageable pageable );
	Optional<List<TaskModel>> findAllByOwnerAndUpdatedBefore( UserModel owner, Timestamp updated, Pageable pageable );
	Optional<List<TaskModel>> findAllByOwner( UserModel owner, Pageable pageable );
}
