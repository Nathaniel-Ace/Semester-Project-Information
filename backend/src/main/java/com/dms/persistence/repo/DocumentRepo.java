package com.dms.persistence.repo;

import com.dms.persistence.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepo extends JpaRepository<Document, Long> {
    Document findByTitle(String name);
}
