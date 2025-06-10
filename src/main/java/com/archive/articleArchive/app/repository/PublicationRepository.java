package com.archive.articleArchive.app.repository;

import com.archive.articleArchive.app.models.Publication;
import com.archive.articleArchive.app.models.PublicationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PublicationRepository extends JpaRepository<Publication, Long> {
    List<Publication> findByTitleContainingIgnoreCase(String title);
    List<Publication> findByStudentFullNameContainingIgnoreCase(String name);
    List<Publication> findByStudentId(Long id);

    @Query("SELECT p FROM Publication p WHERE TYPE(p) = :type")
    List<Publication> findByType(@Param("type") PublicationType type);
}