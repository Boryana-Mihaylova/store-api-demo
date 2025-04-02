package app.survey.service;


import app.survey.client.SurveyClient;


import app.survey.client.dto.Subject;
import app.survey.client.dto.Support;
import app.survey.client.dto.Survey;
import app.survey.client.dto.SurveyRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


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

    public Survey submitSurvey(SurveyRequest surveyRequest) {

        Subject surveySubject = Subject.valueOf(surveyRequest.getSubject().toUpperCase());
        Support surveySupport = Support.valueOf(surveyRequest.getSupport().toUpperCase());

        Survey survey = new Survey();
        survey.setSubject(surveySubject.toString());
        survey.setSupport(surveySupport.toString());
        survey.setUserId(surveyRequest.getUserId());

        // Изпращаш данните към Survey API
        return surveyClient.submitSurvey(survey);
    }


    public Survey getSurvey(UUID userId) {
        // Изпраща GET заявка към REST API-то за получаване на анкета по userId
        return surveyClient.getSurvey(userId);
    }





//    public List<Survey> getAllSurveyRequests() {
//        // Извикваме метод за извличане на всички избори от svc-demo
//        ResponseEntity<List<Survey>> httpResponse = surveyClient.getAllSurveys();
//
//        // Преобразуваме резултатите от Survey в SurveyRequest
//        return httpResponse.getBody();
//    }

}
