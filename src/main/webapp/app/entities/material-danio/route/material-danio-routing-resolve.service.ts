import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMaterialDanio } from '../material-danio.model';
import { MaterialDanioService } from '../service/material-danio.service';

export const materialDanioResolve = (route: ActivatedRouteSnapshot): Observable<null | IMaterialDanio> => {
  const id = route.params['id'];
  if (id) {
    return inject(MaterialDanioService)
      .find(id)
      .pipe(
        mergeMap((materialDanio: HttpResponse<IMaterialDanio>) => {
          if (materialDanio.body) {
            return of(materialDanio.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default materialDanioResolve;
