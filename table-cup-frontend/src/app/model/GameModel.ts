export class GameModel {
  uuid: string
  pointsOfTeamBlue: number;
  pointsOfTeamRed: number;
  playersOfBlueTeam: string[];
  playersOfRedTeam: string[];


  constructor(uuid: string, pointsOfTeamBlue: number, pointsOfTeamRed: number, playersOfBlueTeam: string[], playersOfRedTeam: string[]) {
    this.uuid = uuid;
    this.pointsOfTeamBlue = pointsOfTeamBlue;
    this.pointsOfTeamRed = pointsOfTeamRed;
    this.playersOfBlueTeam = playersOfBlueTeam;
    this.playersOfRedTeam = playersOfRedTeam;
  }
}
