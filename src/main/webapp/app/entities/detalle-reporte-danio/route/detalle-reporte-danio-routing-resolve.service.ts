import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDetalleReporteDanio } from '../detalle-reporte-danio.model';
import { DetalleReporteDanioService } from '../service/detalle-reporte-danio.service';

export const detalleReporteDanioResolve = (route: ActivatedRouteSnapshot): Observable<null | IDetalleReporteDanio> => {
  const id = route.params['id'];
  if (id) {
    return inject(DetalleReporteDanioService)
      .find(id)
      .pipe(
        mergeMap((detalleReporteDanio: HttpResponse<IDetalleReporteDanio>) => {
          if (detalleReporteDanio.body) {
            return of(detalleReporteDanio.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default detalleReporteDanioResolve;
