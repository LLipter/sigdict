package com.llipter.sigdict.repository;

import com.llipter.sigdict.entity.ResetPasswordToken;
import org.springframework.data.repository.CrudRepository;

public interface ResetPasswordTokenRepository extends CrudRepository<ResetPasswordToken, Integer> {
    ResetPasswordToken findByToken(String token);
}
