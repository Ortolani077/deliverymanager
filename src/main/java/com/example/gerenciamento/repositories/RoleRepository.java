package com.example.gerenciamento.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.gerenciamento.Entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
