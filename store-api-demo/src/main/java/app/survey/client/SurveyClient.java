package app.survey.client;


import app.survey.client.dto.Survey;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.*;


import java.util.UUID;


@FeignClient(name = "svc-demo", url = "http://localhost:8081/api/v1/surveys")
public interface SurveyClient {

    @PostMapping
    Survey submitSurvey(@RequestBody Survey survey);


    @GetMapping("/user-survey")
    Survey getSurvey(@RequestParam(name = "userId") UUID userId);


}
