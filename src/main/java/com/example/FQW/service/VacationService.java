package com.example.FQW.service;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.ex.RequestException;
import com.example.FQW.models.DB.Vacation;
import com.example.FQW.models.response.AnswerResponse;
import com.example.FQW.models.response.VacationResponse;
import com.example.FQW.repository.VacationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VacationService {

    private final VacationRepo vacationRepo;

    public List<VacationResponse> getVacation(Long cleanerId) {
        List<Vacation> vacations = vacationRepo.getAllByCleanerId(cleanerId);
        return getVacationResponse(vacations);
    }

    public List<VacationResponse> getVacation(CustomUserDetails userDetails) {
        List<Vacation> vacations = vacationRepo.getAllByCleanerId(userDetails.getClient().getId());
        return getVacationResponse(vacations);
    }

    public VacationResponse createVacation(CustomUserDetails userDetails, Long cleanerId, VacationResponse vacationResponse) {
        if (isCorrectDate(vacationResponse.getStartDay()) && isCorrectDate(vacationResponse.getEndDay())) {
            Vacation vacation = new Vacation();
            vacation.setCleanerId(cleanerId);
            vacation.setManagerId(userDetails.getClient().getId());
            vacation.setStartDay(vacationResponse.getStartDay());
            vacation.setEndDay(vacationResponse.getEndDay());
            return getResponse(vacationRepo.save(vacation));
        } else {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Указаны неверные даты отпуска");
        }
    }

    public AnswerResponse deleteVacation(Long vacationId) {
        vacationRepo.deleteById(vacationId);

        if (vacationRepo.findById(vacationId).isEmpty()) {
            return new AnswerResponse("Запись об отпуске была успешно удалена");
        } else {
            throw new RequestException(HttpStatus.NOT_IMPLEMENTED, "Не удалось удалить запись об отпуске сотрудника");
        }
    }

    private List<VacationResponse> getVacationResponse(List<Vacation> vacations) {
        return vacations.stream()
                .map(this::getResponse)
                .collect(Collectors.toList());
    }

    private VacationResponse getResponse(Vacation vacation) {
        return VacationResponse
                .builder()
                .startDay(vacation.getStartDay())
                .endDay(vacation.getEndDay())
                .build();
    }

    private boolean isCorrectDate(Date date) {
        return (date != null &&
                !date.before(new java.util.Date()));
    }
}
