import {StatusModel} from './StatusModel';
import {PhaseModel} from './PhaseModel';

export class TournamentModel {
  uuid: string
  name: string;
  status: StatusModel;
  phase: PhaseModel;
  userAllowedToJoinTournament: boolean;
  userDisqualified: boolean;
  userInTournament: boolean

  constructor(uuid: string, name: string, status: StatusModel, phase: PhaseModel, userAllowedToJoinTournament: boolean, userDisqualified: boolean, userInTournament: boolean) {
    this.uuid = uuid;
    this.name = name;
    this.status = status;
    this.phase = phase;
    this.userAllowedToJoinTournament = userAllowedToJoinTournament;
    this.userDisqualified = userDisqualified;
    this.userInTournament = userInTournament;
  }
}
