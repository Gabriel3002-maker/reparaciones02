import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ParametroSistemaComponent } from './list/parametro-sistema.component';
import { ParametroSistemaDetailComponent } from './detail/parametro-sistema-detail.component';
import { ParametroSistemaUpdateComponent } from './update/parametro-sistema-update.component';
import ParametroSistemaResolve from './route/parametro-sistema-routing-resolve.service';

const parametroSistemaRoute: Routes = [
  {
    path: '',
    component: ParametroSistemaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ParametroSistemaDetailComponent,
    resolve: {
      parametroSistema: ParametroSistemaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ParametroSistemaUpdateComponent,
    resolve: {
      parametroSistema: ParametroSistemaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ParametroSistemaUpdateComponent,
    resolve: {
      parametroSistema: ParametroSistemaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default parametroSistemaRoute;
