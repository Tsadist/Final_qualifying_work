package com.example.FQW.repository;

import com.example.FQW.models.DB.CleanersApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CleanerApplicationRepo extends JpaRepository<CleanersApplication, Long> {

    List<CleanersApplication> findAllByStatus(CleanersApplication.Status status);
}
