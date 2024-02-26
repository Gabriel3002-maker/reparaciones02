import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMachinery } from '../machinery.model';
import { MachineryService } from '../service/machinery.service';

export const machineryResolve = (route: ActivatedRouteSnapshot): Observable<null | IMachinery> => {
  const id = route.params['id'];
  if (id) {
    return inject(MachineryService)
      .find(id)
      .pipe(
        mergeMap((machinery: HttpResponse<IMachinery>) => {
          if (machinery.body) {
            return of(machinery.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default machineryResolve;
