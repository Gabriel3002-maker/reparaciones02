import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CatalogoItemComponent } from './list/catalogo-item.component';
import { CatalogoItemDetailComponent } from './detail/catalogo-item-detail.component';
import { CatalogoItemUpdateComponent } from './update/catalogo-item-update.component';
import CatalogoItemResolve from './route/catalogo-item-routing-resolve.service';

const catalogoItemRoute: Routes = [
  {
    path: '',
    component: CatalogoItemComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CatalogoItemDetailComponent,
    resolve: {
      catalogoItem: CatalogoItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CatalogoItemUpdateComponent,
    resolve: {
      catalogoItem: CatalogoItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CatalogoItemUpdateComponent,
    resolve: {
      catalogoItem: CatalogoItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default catalogoItemRoute;
