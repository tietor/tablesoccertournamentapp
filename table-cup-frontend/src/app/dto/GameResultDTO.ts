export class GameResultDTO {
  pointOfTeamBlue: number;
  pointOfTeamRed: number;

  constructor(pointOfTeamBlue: number, pointOfTeamRed: number) {
    this.pointOfTeamBlue = pointOfTeamBlue;
    this.pointOfTeamRed = pointOfTeamRed;
  }
}
