package app.survey.client.dto;

import lombok.Builder;
import lombok.Data;


import java.util.UUID;

@Data
@Builder
public class UpsertSurveyPreference {

    private UUID userId;

    private String subject;

    private String support;

}

