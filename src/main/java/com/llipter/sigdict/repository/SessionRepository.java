package com.llipter.sigdict.repository;

import com.llipter.sigdict.entity.Session;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session, Integer> {
    Session findBySessionId(String session_id);
}
