import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { OrdenBodegaComponent } from './list/orden-bodega.component';
import { OrdenBodegaDetailComponent } from './detail/orden-bodega-detail.component';
import { OrdenBodegaUpdateComponent } from './update/orden-bodega-update.component';
import OrdenBodegaResolve from './route/orden-bodega-routing-resolve.service';

const ordenBodegaRoute: Routes = [
  {
    path: '',
    component: OrdenBodegaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrdenBodegaDetailComponent,
    resolve: {
      ordenBodega: OrdenBodegaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrdenBodegaUpdateComponent,
    resolve: {
      ordenBodega: OrdenBodegaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrdenBodegaUpdateComponent,
    resolve: {
      ordenBodega: OrdenBodegaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ordenBodegaRoute;
