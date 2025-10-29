import {Component, Input} from '@angular/core';
import {ParticipantModel} from '../../model/ParticipantModel';
import {
  IonButton,
  IonButtons,
  IonContent,
  IonHeader, IonItem, IonList, IonText,
  IonTitle,
  IonToolbar,
  ModalController
} from '@ionic/angular/standalone';
import {NgForOf} from '@angular/common';

@Component({
  selector: 'app-display-participants',
  templateUrl: './display-participants.component.html',
  styleUrls: ['./display-participants.component.scss'],
  imports: [
    IonButton,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonButtons,
    IonContent,
    IonList,
    IonItem,
    NgForOf,
    IonText
  ]
})
export class DisplayParticipantsComponent {

  @Input() participants: ParticipantModel[] = [];
  @Input() title = '';

  constructor(private modalCtrl: ModalController) {
  }

  cancel(): void {
    this.modalCtrl.dismiss(null, 'cancel').then();
  }

}
