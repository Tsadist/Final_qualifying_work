package com.example.FQW.repository;

import com.example.FQW.models.DB.AdditionService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

public interface AdditionServiceRepo extends JpaRepository<AdditionService, Long> {

}
