package org.vardb.users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    //Page<User> findByLastname(String lastname, Pageable pageable);
	List<User> findByLastname(String lastname);
}