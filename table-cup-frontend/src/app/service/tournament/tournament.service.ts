import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {TournamentDTO} from '../../dto/TournamentDTO';
import {TournamentModel} from '../../model/TournamentModel';
import {ParticipantModel} from '../../model/ParticipantModel';

@Injectable({
  providedIn: 'root'
})
export class TournamentService {

  constructor(private http: HttpClient) {
  }

  public addTournament(tournamentDTO: TournamentDTO): Observable<void> {
    return this.http.post<void>(environment.apiUrl + '/tournaments', tournamentDTO);
  }

  public getALlTournaments(): Observable<TournamentModel[]> {
    return this.http.get<TournamentModel[]>(environment.apiUrl + '/tournaments');
  }

  public joinUserToTournament(uuid: string): Observable<void> {
    return this.http.post<void>(environment.apiUrl + '/tournaments/' + uuid + '/participants', null);
  }

  public getAllParticipantsOfTournament(uuid: string): Observable<ParticipantModel[]> {
    return this.http.get<ParticipantModel[]>(environment.apiUrl + '/tournaments/' + uuid + '/participants/all');
  }

  public getParticipantsOfCurrentPhase(uuid: string): Observable<ParticipantModel[]> {
    return this.http.get<ParticipantModel[]>(environment.apiUrl + '/tournaments/' + uuid + '/participants/current');
  }

  public closeTournament(uuid: string): Observable<void> {
    return this.http.put<void>(environment.apiUrl + '/tournaments/' + uuid + '/close', null);
  }

  public startTournament(uuid: string): Observable<void> {
    return this.http.post<void>(environment.apiUrl + '/tournaments/' + uuid + '/start', null);
  }

  public startNextPhase(uuid: string): Observable<void> {
    return this.http.post<void>(environment.apiUrl + '/tournaments/' + uuid + '/phases/next', null);
  }
}
