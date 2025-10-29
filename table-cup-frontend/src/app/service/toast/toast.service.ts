import {Injectable} from '@angular/core';
import {ToastController} from '@ionic/angular/standalone';

@Injectable({
  providedIn: 'root'
})
export class ToastService {

  constructor(private toastController: ToastController) {
  }

  public showToast(message: string, color: 'success' | 'danger', duration?: number): void {
    this.toastController.create({
      message: message,
      duration: duration == null ? 4000 : duration,
      color: color
    }).then(toast => toast.present());
  }
}
