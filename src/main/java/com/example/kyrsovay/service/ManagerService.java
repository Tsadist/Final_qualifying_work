package com.example.kyrsovay.service;

import com.example.kyrsovay.config.ClientUserDetails;
import com.example.kyrsovay.models.Cleaner;
import com.example.kyrsovay.repository.CleanerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ManagerService {

    private  final CleanerRepo cleanerRepo;

    public List<Cleaner> getSortedListCleaner(ClientUserDetails userDetails) {
        List<Cleaner> cleanerList = cleanerRepo.findAll();
        cleanerList.sort(Comparator.comparing(Cleaner::getName));
        return cleanerList;
    }

}
