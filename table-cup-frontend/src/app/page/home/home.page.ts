import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {
  IonButton,
  IonCard,
  IonCardHeader,
  IonCardTitle,
  IonContent,
  IonIcon,
  IonImg,
  IonItem,
} from '@ionic/angular/standalone';
import {Router} from '@angular/router';
import {addIcons} from 'ionicons';
import {logInOutline, personAddOutline} from 'ionicons/icons';

@Component({
  selector: 'app-home',
  templateUrl: './home.page.html',
  styleUrls: ['./home.page.scss'],
  standalone: true,
  imports: [IonContent, CommonModule, FormsModule, IonButton, IonCard, IonCardHeader, IonCardTitle, IonImg, IonItem, IonIcon]
})
export class HomePage {

  constructor(private router: Router) {
    addIcons({logInOutline, personAddOutline});
  }

  loadLoginPage(): void {
    this.router.navigate(['/login']).then();
  }

  loadRegisterPage() {
    this.router.navigate(['/register']).then();
  }
}
