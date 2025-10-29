import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {GameModel} from '../../model/GameModel';
import {GameResultDTO} from '../../dto/GameResultDTO';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  constructor(private http: HttpClient) {
  }

  public getGamesOfTournamentAndCurrentPhase(uuid: string): Observable<GameModel[]> {
    return this.http.get<GameModel[]>(`http://localhost:8080/tournaments/${uuid}/games`);
  }

  public addResultsOfGame(uuid: string, results: GameResultDTO): Observable<void> {
    return this.http.post<void>(`http://localhost:8080/games/${uuid}/result`, results);
  }
}
