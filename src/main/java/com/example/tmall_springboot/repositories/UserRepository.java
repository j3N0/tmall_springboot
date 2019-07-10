package com.example.tmall_springboot.repositories;

import com.example.tmall_springboot.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
}
