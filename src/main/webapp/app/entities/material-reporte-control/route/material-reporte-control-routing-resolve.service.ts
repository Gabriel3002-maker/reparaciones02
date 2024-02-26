import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMaterialReporteControl } from '../material-reporte-control.model';
import { MaterialReporteControlService } from '../service/material-reporte-control.service';

export const materialReporteControlResolve = (route: ActivatedRouteSnapshot): Observable<null | IMaterialReporteControl> => {
  const id = route.params['id'];
  if (id) {
    return inject(MaterialReporteControlService)
      .find(id)
      .pipe(
        mergeMap((materialReporteControl: HttpResponse<IMaterialReporteControl>) => {
          if (materialReporteControl.body) {
            return of(materialReporteControl.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default materialReporteControlResolve;
