package com.example.FQW.controller;


import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.controller.models.NewCleanerModel;
import com.example.FQW.controller.utils.Randomizer;
import com.example.FQW.models.DB.User;
import com.example.FQW.models.enums.UserRole;
import com.example.FQW.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ManagerController {

    private final UserRepo userRepo;
//    private final CleanerRepo cleanerRepo;

//    @GetMapping("/createNewCleaner")
//    public String getCreateNewCleaner() {
//        return "createNewCleaner";
//    }

    @PostMapping("/createNewCleaner")
    public String postCreateNewCleaner(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       NewCleanerModel newCleanerModel) {
        User cleaner = new User();
        if (userRepo.findByEmail(newCleanerModel.getEmail()) == null) {
            cleaner.setUserRole(UserRole.CLEANER);
            cleaner.setEmail(newCleanerModel.getEmail());
            cleaner.setPhoneNumber(newCleanerModel.getPhoneNumber());
            cleaner.setPassword(Randomizer.getRandomString());
            cleaner.setName(newCleanerModel.getName());
            cleaner.setSurname(newCleanerModel.getSurname());

            userRepo.save(cleaner);
        }

        return "redirect:/profile";
    }

    @GetMapping("/cleaner")
    public String getCleaner() {
        return "/cleaner";
    }


}
