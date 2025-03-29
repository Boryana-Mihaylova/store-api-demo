package app.web.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class CreateReview {

    @NotBlank
    @Size(min = 3, max = 25, message = "Please enter a valid ownerItem!")
    private String ownerItem;

    @NotBlank
    @Size(min = 3, max = 25, message = "Description length must be between 3 and 25 characters!")
    private String description;


}
