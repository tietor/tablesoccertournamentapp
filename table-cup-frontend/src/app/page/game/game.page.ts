import {Component, inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {
  AlertController, IonBackButton,
  IonButton, IonButtons,
  IonCard,
  IonCardContent,
  IonCardHeader,
  IonCardTitle,
  IonContent,
  IonHeader, IonIcon,
  IonTitle,
  IonToolbar
} from '@ionic/angular/standalone';
import {ActivatedRoute} from '@angular/router';
import {GameService} from '../../service/game/game.service';
import {GameModel} from '../../model/GameModel';
import {ToastService} from '../../service/toast/toast.service';
import {GameResultDTO} from '../../dto/GameResultDTO';
import {AuthService} from '../../service/auth/auth.service';

@Component({
  selector: 'app-game',
  templateUrl: './game.page.html',
  styleUrls: ['./game.page.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, IonCard, IonCardHeader, IonCardTitle, IonCardContent, IonButton, IonButtons, IonBackButton, IonIcon]
})
export class GamePage {

  private activatedRoute = inject(ActivatedRoute);
  protected games: GameModel[] = [];
  private tournamentUuid: string = '';

  constructor(private gameService: GameService, private toastService: ToastService, private alertController: AlertController, private authService: AuthService) {
    this.activatedRoute.params.subscribe(params => {
      this.tournamentUuid = params['uuid'];
      this.getGamesOfTournamentAndCurrentPhase()
    })
  }

  private getGamesOfTournamentAndCurrentPhase(): void {
    this.gameService.getGamesOfTournamentAndCurrentPhase(this.tournamentUuid).subscribe({
      next: (games: GameModel[]) => {
        this.games = games;
      }, error: () => {
        this.toastService.showToast('Fehler beim Laden der Spiele', 'danger');
      }
    });
  }

  protected displayAddResultsModal(game: GameModel, operation: string): void {
    const isOperationCreate = operation === 'create';
    const isOperationUpdate = operation === 'update';
    this.alertController.create({
      header: isOperationCreate ? 'Resultat hinzufügen' : 'Resultat anpassen',
      subHeader: isOperationUpdate ? 'Vorheriges Resultat: ' : undefined,
      message: isOperationUpdate ? `Team Blau: ${game.pointsOfTeamBlue}, Team Rot ${game.pointsOfTeamRed}` : undefined,
      buttons: [
        {
          text: 'Abbrechen',
          role: 'cancel',
          handler: () => {
          },
        },
        {
          text: 'Bestätigen',
          role: 'confirm',
          handler: (result) => {
            this.updateGameResults(game.uuid, result);
          },
        },
      ],
      inputs: [
        {
          type: 'number',
          placeholder: 'Resultat Team Blau',
          min: 0,
          max: 20,
          value: isOperationCreate ? null : game.pointsOfTeamBlue,
          id: 'result-team-blue'
        },
        {
          type: 'number',
          placeholder: 'Resultat Team Rot',
          min: 0,
          max: 20,
          value: isOperationCreate ? null : game.pointsOfTeamRed,
          id: 'result-team-red'
        }
      ],
    }).then(alert => {
      alert.present().then();
    });
  }

  private updateGameResults(gameUuid: string, result: string[]) {
    let resultTeamBlue: number = parseInt(result[0]);
    let resultTeamRed: number = parseInt(result[1]);
    if (isNaN(resultTeamBlue) || isNaN(resultTeamRed)) {
      this.toastService.showToast('Bitte Resultate eintragen!', 'danger');
      return;
    }
    if (resultTeamBlue > 20 || resultTeamRed > 20) {
      this.toastService.showToast('Resultat kann nicht höher als 50 sein', 'danger');
      return;
    }
    if (resultTeamBlue < 0 || resultTeamRed < 0) {
      this.toastService.showToast('Resultat kann nicht niedriger als 0 sein', 'danger');
      return;
    }
    const results = new GameResultDTO(resultTeamBlue, resultTeamRed);
    this.gameService.addResultsOfGame(gameUuid, results).subscribe({
      next: () => {
        this.toastService.showToast('Resultat wurde erfolgreich eingetragen!', 'success');
        this.getGamesOfTournamentAndCurrentPhase();
      },
      error: () => {
        this.toastService.showToast('Fehler beim Aktualisieren der Resultate!', 'danger');
      }
    });
  }

  logout(): void {
    this.authService.logout();
  }
}
