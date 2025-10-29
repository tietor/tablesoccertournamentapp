import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {RegisterDTO} from '../../dto/RegisterDTO';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  constructor(private http: HttpClient) {
  }

  public register(registerDTO: RegisterDTO): Observable<void> {
    return this.http.post<void>(environment.apiUrl + '/users', registerDTO);
  }
}
