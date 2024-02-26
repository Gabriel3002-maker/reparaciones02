import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MaterialDanioComponent } from './list/material-danio.component';
import { MaterialDanioDetailComponent } from './detail/material-danio-detail.component';
import { MaterialDanioUpdateComponent } from './update/material-danio-update.component';
import MaterialDanioResolve from './route/material-danio-routing-resolve.service';

const materialDanioRoute: Routes = [
  {
    path: '',
    component: MaterialDanioComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MaterialDanioDetailComponent,
    resolve: {
      materialDanio: MaterialDanioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MaterialDanioUpdateComponent,
    resolve: {
      materialDanio: MaterialDanioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MaterialDanioUpdateComponent,
    resolve: {
      materialDanio: MaterialDanioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default materialDanioRoute;
