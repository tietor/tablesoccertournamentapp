package ch.teko.tablecupbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTO {

  @NotBlank(message = "Username cannot be blank")
  @Size(min = 5, max = 10, message = "Username must be between 5 and 10 characters long")
  private String username;

  @NotBlank(message = "Password cannot be blank")
  @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters long")
  private String password;

  @NotBlank(message = "Role cannot be blank")
  private String role;
}
