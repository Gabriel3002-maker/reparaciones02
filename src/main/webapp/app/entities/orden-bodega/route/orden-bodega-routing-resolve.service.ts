import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrdenBodega } from '../orden-bodega.model';
import { OrdenBodegaService } from '../service/orden-bodega.service';

export const ordenBodegaResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrdenBodega> => {
  const id = route.params['id'];
  if (id) {
    return inject(OrdenBodegaService)
      .find(id)
      .pipe(
        mergeMap((ordenBodega: HttpResponse<IOrdenBodega>) => {
          if (ordenBodega.body) {
            return of(ordenBodega.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default ordenBodegaResolve;
