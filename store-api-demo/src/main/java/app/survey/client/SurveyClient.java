package app.survey.client;


import app.survey.client.dto.SurveyPreference;

import app.survey.client.dto.SurveyRequest;
import app.survey.client.dto.UpsertSurveyPreference;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;


@FeignClient(name = "svc-demo", url = "http://localhost:8081/api/v1/surveys")
public interface SurveyClient {


    @PostMapping("/user-support")
    ResponseEntity<Void> upsertSurveyPreference(@RequestBody UpsertSurveyPreference surveyPreference);

    @GetMapping("/user-support")
    ResponseEntity<SurveyPreference> getUserPreference(@RequestParam(name = "userId") UUID userId);

//    @GetMapping
//    ResponseEntity<List<Survey>> getSurveyHistory(@RequestParam(name = "userId")UUID userId);
//
    @PostMapping
    ResponseEntity<Void> sendSurvey(@RequestBody SurveyRequest surveyRequest);


}
