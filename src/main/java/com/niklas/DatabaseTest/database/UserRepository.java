package com.niklas.DatabaseTest.database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUID(int UID);
    User findByUsername(String username);
    User findByEmail(String email);
}