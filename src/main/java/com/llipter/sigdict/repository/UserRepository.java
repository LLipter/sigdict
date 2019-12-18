package com.llipter.sigdict.repository;

import org.springframework.data.repository.CrudRepository;
import com.llipter.sigdict.entity.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsername(String username);
}