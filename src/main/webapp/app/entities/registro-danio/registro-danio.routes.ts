import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RegistroDanioComponent } from './list/registro-danio.component';
import { RegistroDanioDetailComponent } from './detail/registro-danio-detail.component';
import { RegistroDanioUpdateComponent } from './update/registro-danio-update.component';
import RegistroDanioResolve from './route/registro-danio-routing-resolve.service';

const registroDanioRoute: Routes = [
  {
    path: '',
    component: RegistroDanioComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RegistroDanioDetailComponent,
    resolve: {
      registroDanio: RegistroDanioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RegistroDanioUpdateComponent,
    resolve: {
      registroDanio: RegistroDanioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RegistroDanioUpdateComponent,
    resolve: {
      registroDanio: RegistroDanioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default registroDanioRoute;
