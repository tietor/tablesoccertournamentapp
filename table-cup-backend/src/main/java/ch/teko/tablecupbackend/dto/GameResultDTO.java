package ch.teko.tablecupbackend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameResultDTO {

  @NotNull(message = "points of team blue cannot be empty")
  @Min(value = 0, message = "points of team blue must be greater than or equal to 0")
  @Max(value = 20, message = "points of team blue must be less than or equal to 20")
  private int pointOfTeamBlue;

  @NotNull(message = "points of team red cannot be empty")
  @Min(value = 0, message = "points of team red must be greater than or equal to 0")
  @Max(value = 20, message = "points of team red must be less than or equal to 20")
  private int pointOfTeamRed;
}
