import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDetalleDanio } from '../detalle-danio.model';
import { DetalleDanioService } from '../service/detalle-danio.service';

export const detalleDanioResolve = (route: ActivatedRouteSnapshot): Observable<null | IDetalleDanio> => {
  const id = route.params['id'];
  if (id) {
    return inject(DetalleDanioService)
      .find(id)
      .pipe(
        mergeMap((detalleDanio: HttpResponse<IDetalleDanio>) => {
          if (detalleDanio.body) {
            return of(detalleDanio.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default detalleDanioResolve;
