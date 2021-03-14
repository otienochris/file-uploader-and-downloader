package com.otienochris.filemanipulator.repositories;

import com.otienochris.filemanipulator.Models.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepo extends JpaRepository<Document, Long> {
    @Query("SELECT new Document(d.id, d.name, d.size) FROM Document d ORDER BY d.uploadTime DESC ")
    List<Document> findAll();
}
