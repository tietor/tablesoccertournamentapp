import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {LoginDTO} from '../../dto/LoginDTO';
import {BehaviorSubject, Observable, tap} from 'rxjs';
import {environment} from '../../../environments/environment';
import {UserModel} from '../../model/UserModel';
import {Router} from '@angular/router';
import {ToastService} from '../toast/toast.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private currentUser: UserModel | null;
  private readonly USER_KEY: string = 'currentUser';

  constructor(private http: HttpClient, private router: Router, private toastService: ToastService) {
    const user: string | null = localStorage.getItem(this.USER_KEY);
    if (user) {
      this.currentUser = JSON.parse(user);
    } else {
      this.currentUser = null;
    }
  }




  login(loginDTO: LoginDTO): Observable<void> {
    return this.http.post<void>(environment.apiUrl + '/auth/sessions', loginDTO).pipe(tap((userModel: any) => {
      localStorage.setItem(this.USER_KEY, JSON.stringify(userModel));
      this.currentUser = userModel;
    }));
  }

  public isUserLoggedIn(): boolean {
    return !!this.currentUser;
  }

  public isUserTournamentDirector(): boolean {
    return this.currentUser?.role === 'TOURNAMENT_DIRECTOR';
  }

  public isUserAttendee(): boolean {
    return this.currentUser?.role === 'ATTENDEE';
  }


  public logout(): void {
    this.http.get<void>(environment.apiUrl + '/logout').subscribe({
      next: () => {
        localStorage.removeItem(this.USER_KEY);
        this.currentUser = null;
        this.router.navigate(['/login']).then();
      },
      error: exception => {
        this.toastService.showToast('Logout nicht erfolgeich!', 'danger');
        console.error(exception);
      }
    });
  }


}
