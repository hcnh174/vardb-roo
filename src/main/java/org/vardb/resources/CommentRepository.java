package org.vardb.resources;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CommentRepository extends JpaRepository<Comment, Long> {

	//@Query("select c from Comment c where c.type = :type or c.identifier = :identifier")
	//List<Comment> findByTypeAndIdentifier(@Param("type") String type, @Param("identifier") String identifier, Pageable pageable);
	
	List<Comment> findByTypeAndIdentifier(String type, String identifier);
	//List<Comment> findByTypeAndIdentifier(String type, String identifier, Pageable pageable);
}