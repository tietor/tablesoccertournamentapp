import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {
  IonButton,
  IonCard,
  IonCardContent,
  IonCardHeader,
  IonCardTitle,
  IonContent,
  IonGrid,
  IonIcon,
  IonInput,
  IonItem,
  IonLabel,
  IonRow
} from '@ionic/angular/standalone';
import {addIcons} from 'ionicons';
import {logInOutline, personAddOutline} from 'ionicons/icons';
import {Router} from '@angular/router';
import {AuthService} from '../../service/auth/auth.service';
import {ToastService} from '../../service/toast/toast.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
  standalone: true,
  imports: [IonContent, CommonModule, ReactiveFormsModule, IonItem, IonLabel, IonInput, IonButton, IonCard, IonCardContent, IonCardHeader, IonCardTitle, IonIcon, IonGrid, IonRow, ReactiveFormsModule]
})
export class LoginPage {
  loginForm: FormGroup;

  constructor(private router: Router, private authService: AuthService, private toastService: ToastService) {
    addIcons({logInOutline, personAddOutline});
    this.loginForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required])
    });
  }

  login(): void {
    this.loginForm.markAllAsTouched();
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value).subscribe({
        next: () => {
          this.router.navigate(['/tournaments']).then(() => this.loginForm.reset());
        },
        error: error => {
          if (error.status === 401) {
            this.toastService.showToast('Benutzername oder Passwort falsch!', 'danger');
          }
        }
      })
    }
  }

  redirectToRegisterPage(): void {
    this.router.navigate(['/register']).then();
  }
}
