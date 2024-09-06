/**
 * 
 */
package com.example.repository;

import com.example.entity.FilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 
 */
@Repository
public interface FilesRepository extends JpaRepository<FilesEntity, Long> {
    Optional<FilesEntity> findByFileName(String s);
}
