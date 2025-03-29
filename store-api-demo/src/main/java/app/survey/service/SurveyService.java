package app.survey.service;


import app.survey.client.SurveyClient;


import app.survey.client.dto.SurveyPreference;

import app.survey.client.dto.SurveyRequest;

import app.survey.client.dto.UpsertSurveyPreference;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Slf4j
@Service
public class SurveyService {

    private final SurveyClient surveyClient;


    @Autowired
    public SurveyService(SurveyClient surveyClient) {
        this.surveyClient = surveyClient;
    }



    public void saveSurveyPreference(UUID userId, String subject, String support) {

        UpsertSurveyPreference surveyPreference = UpsertSurveyPreference.builder()
                .userId(userId)
                .subject(subject)
                .support(support)
                .build();

        // Invoke Feign client and execute HTTP Post Request.
        try {
            ResponseEntity<Void> httpResponse = surveyClient.upsertSurveyPreference(surveyPreference);
            if (!httpResponse.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to svc-demo failed] Can't save user preference for user with id = [%s]".formatted(userId));
            }
        } catch (Exception e) {
            log.error("Unable to call svc-demo.");
        }
    }

    public SurveyPreference getSurveyPreference(UUID userId) {

        UpsertSurveyPreference surveyPreference = UpsertSurveyPreference.builder()
                .userId(userId)

                .build();

        ResponseEntity<SurveyPreference> httpResponse = surveyClient.getUserPreference(userId);

        if (!httpResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Survey preference for user id [%s] does not exist.".formatted(userId));
        }

        return httpResponse.getBody();
    }


    public void sendSurvey(UUID userId, String subject, String support) {

        SurveyRequest surveyRequest = SurveyRequest.builder()
                .userId(userId)
                .subject(subject)
                .support(support)
                .build();

        // Servive to Service
        ResponseEntity<Void> httpResponse;
        try {
            httpResponse = surveyClient.sendSurvey(surveyRequest);
            if (!httpResponse.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to svc-demo failed] Can't send email to user with id = [%s]".formatted(userId));
            }
        } catch (Exception e) {
            log.warn("Can't send email to user with id = [%s] due to 500 Internal Server Error.".formatted(userId));
        }
    }

}
