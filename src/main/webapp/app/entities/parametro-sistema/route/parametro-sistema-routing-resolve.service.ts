import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IParametroSistema } from '../parametro-sistema.model';
import { ParametroSistemaService } from '../service/parametro-sistema.service';

export const parametroSistemaResolve = (route: ActivatedRouteSnapshot): Observable<null | IParametroSistema> => {
  const id = route.params['id'];
  if (id) {
    return inject(ParametroSistemaService)
      .find(id)
      .pipe(
        mergeMap((parametroSistema: HttpResponse<IParametroSistema>) => {
          if (parametroSistema.body) {
            return of(parametroSistema.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default parametroSistemaResolve;
