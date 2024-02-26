import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICatalogoItem } from '../catalogo-item.model';
import { CatalogoItemService } from '../service/catalogo-item.service';

export const catalogoItemResolve = (route: ActivatedRouteSnapshot): Observable<null | ICatalogoItem> => {
  const id = route.params['id'];
  if (id) {
    return inject(CatalogoItemService)
      .find(id)
      .pipe(
        mergeMap((catalogoItem: HttpResponse<ICatalogoItem>) => {
          if (catalogoItem.body) {
            return of(catalogoItem.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default catalogoItemResolve;
