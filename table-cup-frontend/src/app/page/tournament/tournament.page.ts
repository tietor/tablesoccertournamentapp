import {Component} from '@angular/core';
import {
  IonAlert,
  IonButton, IonButtons,
  IonCard,
  IonCardContent,
  IonCardHeader,
  IonCardTitle,
  IonContent,
  IonFab,
  IonHeader,
  IonIcon,
  IonItem,
  IonText, IonThumbnail,
  IonTitle,
  IonToolbar,
  ModalController
} from '@ionic/angular/standalone';
import {AuthService} from '../../service/auth/auth.service';
import {addIcons} from 'ionicons';
import {addOutline, logOutOutline} from 'ionicons/icons';
import {ToastService} from '../../service/toast/toast.service';
import {TournamentService} from '../../service/tournament/tournament.service';
import {TournamentDTO} from '../../dto/TournamentDTO';
import {NgForOf, NgIf} from '@angular/common';
import {HttpErrorResponse} from '@angular/common/http';
import {TournamentModel} from '../../model/TournamentModel';
import {DisplayParticipantsComponent} from '../../component/display-participants/display-participants.component';
import {Router} from '@angular/router';

@Component({
  selector: 'app-tournament',
  templateUrl: 'tournament.page.html',
  styleUrls: ['tournament.page.scss'],
  imports: [IonHeader, IonToolbar, IonTitle, IonContent, IonButton, IonIcon, IonFab, IonAlert, IonCard, NgForOf, IonCardHeader, IonCardTitle, IonCardContent, NgIf, IonItem, IonText, IonButtons, IonThumbnail],
})
export class TournamentPage {
  public alertButtons = [
    {
      text: 'Abbrechen',
      role: 'cancel',
    },
    {
      text: 'Erstellen',
      role: 'confirm',
      handler: (tournamentDTO: TournamentDTO) => {
        if (tournamentDTO.name.trim() === '') {
          this.toastService.showToast('Turnier konnte nicht erstellt werden. Bitte Name hinzufügen!', 'danger');
        } else {
          this.addTournament(tournamentDTO);
        }
        this.clearInputFieldOfAlert();
        this.getTournaments();
      }
    },
  ];

  public alertInputs = [
    {
      id: 'tournamentName',
      name: 'name',
      placeholder: 'Turniername',
      attributes: {
        maxLength: 30,
      }
    }
  ]

  protected tournaments: TournamentModel[] = []

  constructor(protected authService: AuthService, private toastService: ToastService, private tournamentService: TournamentService, private modalController: ModalController, private router: Router) {
    addIcons({addOutline, logOutOutline});
    this.getTournaments();
  }

  logout(): void {
    this.authService.logout();
  }

  protected clearInputFieldOfAlert() {
    const tournamentNameInputField = document.getElementById('tournamentName') as HTMLInputElement;
    tournamentNameInputField.value = ''
  }

  private getTournaments(): void {
    this.tournamentService.getALlTournaments().subscribe(tournaments => this.tournaments = tournaments);
  }

  private addTournament(tournament: TournamentDTO): void {
    this.tournamentService.addTournament(tournament).subscribe({
      next: () => {
        this.toastService.showToast('Turnier wurde erfolgreich erstellt!', 'success');
        this.getTournaments();
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 409 || error.status === 400) {
          this.toastService.showToast(error.error, 'danger');
        } else {
          this.toastService.showToast('Turnier konnte nicht erstellt werden!', 'danger');
        }
      }
    });
  }

  joinUserToTournament(tournamentModel: TournamentModel): void {
    this.tournamentService.joinUserToTournament(tournamentModel.uuid).subscribe({
      next: () => {
        this.toastService.showToast('Du bist nun Teil des Turniers!', 'success');
        tournamentModel.userAllowedToJoinTournament = false
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 404) {
          this.toastService.showToast(error.error, 'danger');
        } else if (error.status === 400) {
          this.toastService.showToast(error.error, 'danger');
        } else {
          this.toastService.showToast('Fehler beim Beitreten zum Turnier!', 'danger');
        }
      }
    })
  }

  displayAllParticipants(uuid: string): void {
    this.tournamentService.getAllParticipantsOfTournament(uuid).subscribe({
      next: (participants) => {
        this.modalController.create({
          component: DisplayParticipantsComponent,
          componentProps: {
            participants: participants,
            title: 'Alle Teilnehmer'
          }
        }).then(modal => modal.present());
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 404) {
          this.toastService.showToast(error.error, 'danger');
        } else {
          this.toastService.showToast('Fehler beim Laden der Teilnehmer!', 'danger');
        }
      }
    });
  }

  startTournament(uuid: string): void {
    this.tournamentService.startTournament(uuid).subscribe({
      next: () => {
        this.toastService.showToast('Turnier wurde gestartet!', 'success');
        this.getTournaments();
      }, error: (error: HttpErrorResponse) => {
        if (error.status === 400) {
          this.toastService.showToast(error.error, 'danger');
        } else {
          this.toastService.showToast('Starten vom Turnier fehlgeschlagen!', 'danger');
        }
      }
    })
  }

  displayGamePage(uuid: string) {
    this.router.navigate(['/games', uuid]).then();
  }

  startNextPhase(uuid: string) {
    this.tournamentService.startNextPhase(uuid).subscribe({
      next: () => {
        this.toastService.showToast('Nächste Phase wurde erfolgreich gestartet!', 'success');
        this.getTournaments();
      }, error: (error: HttpErrorResponse) => {
        if (error.status === 400 || error.status === 404) {
          this.toastService.showToast(error.error, 'danger');
        } else {
          this.toastService.showToast('Etwas ist schief gelaufen', 'danger');
        }
      }
    })
  }

  isStatusOpen(tournament: TournamentModel) {
    return tournament.status.internalStatus ==='OPEN';
  }

  isPhaseFinal(tournament: TournamentModel) {
    return tournament.phase.internalPhase === 'FINAL';
  }

  isStatusRunning(tournament: TournamentModel) {
    return tournament.status.internalStatus ==='RUNNING';
  }

  displayParticipantsInCurrentPhase(uuid: string) {
    this.tournamentService.getParticipantsOfCurrentPhase(uuid).subscribe({
      next: (participants) => {
        this.modalController.create({
          component: DisplayParticipantsComponent,
          componentProps: {
            participants: participants,
            title: 'Aktuelle Teilnehmer'
          }
        }).then(modal => modal.present());
      }, error: (error: HttpErrorResponse) => {
        if (error.status === 404) {
          this.toastService.showToast(error.error, 'danger');
        } else {
          this.toastService.showToast('Fehler beim Laden der aktuellen Teilnehmer!', 'danger');
        }
      }
    })
  }

  closeTournament(uuid: string) {
    this.tournamentService.closeTournament(uuid).subscribe({
      next: () => {
        this.toastService.showToast('Turnier wurde geschlossen!', 'success');
        this.getTournaments();
      }, error: (error) => {
        if (error.status === 400) {
          this.toastService.showToast(error.error, 'danger');
        } else {
          this.toastService.showToast('Fehler beim Abschliessen vom Turnier!', 'danger');
        }
      }
    })
  }
}
