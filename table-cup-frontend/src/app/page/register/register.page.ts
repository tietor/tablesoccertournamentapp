import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
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
  IonRow,
  IonSelect,
  IonSelectOption
} from '@ionic/angular/standalone';
import {addIcons} from 'ionicons';
import {logInOutline, personAddOutline} from 'ionicons/icons';
import {Router} from '@angular/router';
import {RoleService} from '../../service/role/role.service';
import {RoleModel} from '../../model/RoleModel';
import {RegisterService} from '../../service/register/register.service';
import {RegisterDTO} from '../../dto/RegisterDTO';
import {ToastService} from '../../service/toast/toast.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.page.html',
  styleUrls: ['./register.page.scss'],
  standalone: true,
  imports: [IonContent, CommonModule, FormsModule, IonCard, IonCardHeader, IonCardTitle, IonGrid, IonRow, IonCardContent, IonInput, IonItem, IonLabel, IonSelect, IonSelectOption, IonButton, IonIcon, ReactiveFormsModule],
})
export class RegisterPage {
  registerForm: FormGroup;
  roles: RoleModel[] = [];

  constructor(private router: Router, private roleService: RoleService, private registerService: RegisterService, private toastService: ToastService) {
    addIcons({logInOutline, personAddOutline});
    this.registerForm = new FormGroup({
        username: new FormControl('', [Validators.required, Validators.minLength(5), Validators.maxLength(10)]),
        password: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(30)]),
        confirmPassword: new FormControl('', [Validators.required, this.passwordMatchValidator()]),
        role: new FormControl('', [Validators.required])
      },
      {
        validators: [this.passwordMatchValidator()]
      }
    );
    this.fetchRoles()
  }

  private fetchRoles(): void {
    this.roleService.getAllRoles().subscribe(roles => {
      this.roles = roles;
      console.log(this.roles);
    });
  }

  passwordMatchValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const password: string = control.get('password')?.value;
      const confirmPassword: string = control.get('confirmPassword')?.value;
      if (!password || !confirmPassword) {
        return null;
      }
      const arePasswordsEqual = password === confirmPassword;
      return arePasswordsEqual ? null : {passwordMismatch: true};
    }
  }

  register(): void {
    this.registerForm.markAllAsTouched();
    if (this.registerForm.valid) {
      const registerDTO = new RegisterDTO(this.registerForm.value.username, this.registerForm.value.password, this.registerForm.value.role);
      this.registerService.register(registerDTO).subscribe({
        next: () => {
          this.registerForm.reset();
          this.toastService.showToast('Registrierung erfolgreich. Du wirst in 5 Sekunden zur Loginseite weitergeleitet.', 'success');
          setTimeout(() => this.redirectToLoginPage(), 5000);
        }, error: (exception) => {
          if (exception.status === 409 || exception.status === 404) {
            this.toastService.showToast(exception.error, 'danger');
            if (exception.status === 404) {
              // if a role was deleted in a database, reload the page
              window.location.reload();
            }
          }
        }
      });
    }
  }

  redirectToLoginPage(): void {
    this.router.navigate(['/login']).then();
  }
}
