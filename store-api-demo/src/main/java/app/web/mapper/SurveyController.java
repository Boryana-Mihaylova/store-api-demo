package app.web.mapper;



import app.security.AuthenticationMetadata;
import app.survey.client.dto.SurveyPreference;

import app.survey.service.SurveyService;
import app.user.model.User;
import app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


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


    @PutMapping("/user-support")
    public String createUserPreference(@RequestParam(name = "support") String support, String subject, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        surveyService.sendSurvey(authenticationMetadata.getUserId(), subject, support);

        return "redirect:/user-survey";
    }


    @GetMapping
    public ModelAndView getSurveyPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        SurveyPreference surveyPreference = surveyService.getSurveyPreference(user.getId());


        ModelAndView modelAndView = new ModelAndView("user-survey");
        modelAndView.addObject("user", user);
        modelAndView.addObject("surveyPreference", surveyPreference);

        return modelAndView;
    }



}


