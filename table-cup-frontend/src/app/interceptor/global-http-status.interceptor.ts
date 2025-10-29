import {HttpErrorResponse} from '@angular/common/http';
import {ErrorHandler, Injectable} from '@angular/core';
import {ToastService} from '../service/toast/toast.service';
import {AuthService} from '../service/auth/auth.service';


@Injectable({
  providedIn: 'root'
})
export class GlobalHttpStatusInterceptor implements ErrorHandler {


  constructor(private toastService: ToastService, private authService: AuthService) {
  }

  handleError(exception: any): void {
    if (exception instanceof HttpErrorResponse) {
      if (exception.status === 500 || exception.status === 400) {
        this.toastService.showToast(exception.error, 'danger');
      } else if (exception.status === 401) {
        this.authService.logout();
      }
    }
  }
}
