package ch.teko.tablecupbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TournamentDTO {

  @NotBlank(message = "Tournament name cannot be null")
  @Size(max = 30, message = "Tournament name must be between 1 and 30 characters long", min = 1)
  private String name;

}
