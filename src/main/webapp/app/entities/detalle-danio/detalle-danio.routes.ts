import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DetalleDanioComponent } from './list/detalle-danio.component';
import { DetalleDanioDetailComponent } from './detail/detalle-danio-detail.component';
import { DetalleDanioUpdateComponent } from './update/detalle-danio-update.component';
import DetalleDanioResolve from './route/detalle-danio-routing-resolve.service';

const detalleDanioRoute: Routes = [
  {
    path: '',
    component: DetalleDanioComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DetalleDanioDetailComponent,
    resolve: {
      detalleDanio: DetalleDanioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DetalleDanioUpdateComponent,
    resolve: {
      detalleDanio: DetalleDanioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DetalleDanioUpdateComponent,
    resolve: {
      detalleDanio: DetalleDanioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default detalleDanioRoute;
