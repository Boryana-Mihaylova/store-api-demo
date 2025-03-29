package app.web.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Size(min = 3, message = "Username must be at least 3 symbols")
    private String username;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Not a valid email format")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 symbols")
    private String password;

}
