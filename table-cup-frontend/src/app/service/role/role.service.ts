import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs';
import {RoleModel} from 'src/app/model/RoleModel';

@Injectable({
  providedIn: 'root',
})
export class RoleService {

  constructor(private http: HttpClient) {
  }

  public getAllRoles(): Observable<RoleModel[]> {
    return this.http.get<RoleModel[]>(environment.apiUrl + '/roles');
  }
}
