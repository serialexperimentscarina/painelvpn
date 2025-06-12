package com.example.painelvpn.persistence;

import com.example.painelvpn.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByUsernameAndPassword(String username, String password);

  Optional<User> findByUsername(String username);
}
