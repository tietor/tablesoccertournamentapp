import {bootstrapApplication} from '@angular/platform-browser';
import {PreloadAllModules, provideRouter, RouteReuseStrategy, withHashLocation, withPreloading} from '@angular/router';
import {IonicRouteStrategy, provideIonicAngular} from '@ionic/angular/standalone';

import {routes} from './app/app.routes';
import {AppComponent} from './app/app.component';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {GlobalHttpStatusInterceptor} from './app/interceptor/global-http-status.interceptor';
import {ErrorHandler} from '@angular/core';
import {authenticationInterceptor} from './app/interceptor/authentication.interceptor';
import {HashLocationStrategy, LocationStrategy} from '@angular/common';

bootstrapApplication(AppComponent, {
  providers: [
    {provide: RouteReuseStrategy, useClass: IonicRouteStrategy},
    provideIonicAngular(),
    provideHttpClient(withInterceptors([authenticationInterceptor])),
    {
      provide: ErrorHandler,
      useClass: GlobalHttpStatusInterceptor
    },
    provideRouter(routes, withPreloading(PreloadAllModules)),
    {provide: LocationStrategy, useClass: HashLocationStrategy}
  ],
});
