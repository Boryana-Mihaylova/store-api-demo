package app.web.mapper;



import app.security.AuthenticationMetadata;

import app.survey.client.dto.Survey;
import app.survey.client.dto.SurveyRequest;
import app.survey.service.SurveyService;

import app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;


@Controller
@RequestMapping("/surveys")
public class SurveyController {

    private final UserService userService;
    private final SurveyService surveyService;

    @Autowired
    public SurveyController(UserService userService, SurveyService surveyService) {
        this.userService = userService;
        this.surveyService = surveyService;
    }



    @GetMapping("/newSurvey")
    public ModelAndView getNewSurveyPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        UUID userId = authenticationMetadata.getUserId();


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user-support");
        modelAndView.addObject("userId", userId);
        modelAndView.addObject("surveyRequest", SurveyRequest.builder().build());

        return modelAndView;
    }


    @PostMapping
    public String submitSurvey(@RequestParam("subject") String subject,
                               @RequestParam("support") String support,
                               @RequestParam("userId") UUID userId, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {



        SurveyRequest surveyRequest = new SurveyRequest();
        surveyRequest.setSubject(subject);
        surveyRequest.setSupport(support);
        surveyRequest.setUserId(userId);

        Survey survey = surveyService.submitSurvey(surveyRequest);
        UUID surveyId = survey.getId();

        return "redirect:/surveys/user-survey";


    }


    @GetMapping("/user-survey")
    public ModelAndView getSurvey(@RequestParam(name = "userId") UUID userId, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        Survey survey = surveyService.getSurvey(authenticationMetadata.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user-survey");
        modelAndView.addObject("survey", survey);

        return modelAndView;
    }

}


