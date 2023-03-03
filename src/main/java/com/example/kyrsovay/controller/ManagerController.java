package com.example.kyrsovay.controller;


import com.example.kyrsovay.controller.models.NewCleanerModel;
import com.example.kyrsovay.controller.utils.Randomizer;
import com.example.kyrsovay.domain.Cleaner;
import com.example.kyrsovay.domain.Client;
import com.example.kyrsovay.domain.enums.ClientRole;
import com.example.kyrsovay.repository.CleanerRepo;
import com.example.kyrsovay.repository.ClientRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ManagerController {

    private final BaseController baseController;
    private final ClientRepo clientRepo;
    private final CleanerRepo cleanerRepo;

    @GetMapping("/createNewCleaner")
    public String getCreateNewCleaner() {
        return "createNewCleaner";
    }

    @PostMapping("/createNewCleaner")
    public String postCreateNewCleaner(NewCleanerModel newCleanerModel) {
        Client client = new Client();
        Cleaner cleaner = new Cleaner();
        if (clientRepo.findByEmail(newCleanerModel.getEmail()) == null) {
            client.setClientRole(ClientRole.Клинер);
            client.setEmail(newCleanerModel.getEmail());
            client.setPhoneNumber(newCleanerModel.getPhoneNumber());
            client.setPassword(Randomizer.getRandomString());

            Client newClient = clientRepo.save(client);

            cleaner.setClient(newClient);
            cleaner.setId(newClient.getId());
            cleaner.setName(newCleanerModel.getName());
            cleaner.setSurname(newCleanerModel.getSurname());
            cleanerRepo.save(cleaner);
        }

        return "redirect:/profile";
    }

}
