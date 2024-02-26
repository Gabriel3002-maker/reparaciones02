import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRolFuncionalidad } from '../rol-funcionalidad.model';
import { RolFuncionalidadService } from '../service/rol-funcionalidad.service';

export const rolFuncionalidadResolve = (route: ActivatedRouteSnapshot): Observable<null | IRolFuncionalidad> => {
  const id = route.params['id'];
  if (id) {
    return inject(RolFuncionalidadService)
      .find(id)
      .pipe(
        mergeMap((rolFuncionalidad: HttpResponse<IRolFuncionalidad>) => {
          if (rolFuncionalidad.body) {
            return of(rolFuncionalidad.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default rolFuncionalidadResolve;
