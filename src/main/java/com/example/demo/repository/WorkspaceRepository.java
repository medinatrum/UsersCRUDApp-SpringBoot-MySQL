package com.example.demo.repository;

import com.example.demo.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    Optional<Workspace> findWorkspaceById(Long id);

    void deleteById(Long id);

}
