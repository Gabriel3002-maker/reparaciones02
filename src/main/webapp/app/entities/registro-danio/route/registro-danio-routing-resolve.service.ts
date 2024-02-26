import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRegistroDanio } from '../registro-danio.model';
import { RegistroDanioService } from '../service/registro-danio.service';

export const registroDanioResolve = (route: ActivatedRouteSnapshot): Observable<null | IRegistroDanio> => {
  const id = route.params['id'];
  if (id) {
    return inject(RegistroDanioService)
      .find(id)
      .pipe(
        mergeMap((registroDanio: HttpResponse<IRegistroDanio>) => {
          if (registroDanio.body) {
            return of(registroDanio.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default registroDanioResolve;
