package com.relevans.repo;

import com.relevans.repo.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepo extends JpaRepository<UserModel, Integer> {
    Optional<UserModel> findByUsername(String username);
}