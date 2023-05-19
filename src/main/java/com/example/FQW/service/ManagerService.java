package com.example.FQW.service;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.models.DB.User;
import com.example.FQW.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ManagerService {

    private  final UserRepo userRepo;

    public List<User> getSortedListCleaner(CustomUserDetails userDetails) {
        List<User> cleanerList = userRepo.findAll();
        cleanerList.sort(Comparator.comparing(User::getName));
        return cleanerList;
    }

}
