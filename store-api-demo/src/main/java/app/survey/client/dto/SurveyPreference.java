package app.survey.client.dto;

import lombok.Data;

import javax.security.auth.Subject;
import java.util.UUID;

@Data
public class SurveyPreference {


    private String subject;

    private String support;

}

