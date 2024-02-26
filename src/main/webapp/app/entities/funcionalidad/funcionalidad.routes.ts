import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FuncionalidadComponent } from './list/funcionalidad.component';
import { FuncionalidadDetailComponent } from './detail/funcionalidad-detail.component';
import { FuncionalidadUpdateComponent } from './update/funcionalidad-update.component';
import FuncionalidadResolve from './route/funcionalidad-routing-resolve.service';

const funcionalidadRoute: Routes = [
  {
    path: '',
    component: FuncionalidadComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FuncionalidadDetailComponent,
    resolve: {
      funcionalidad: FuncionalidadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FuncionalidadUpdateComponent,
    resolve: {
      funcionalidad: FuncionalidadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FuncionalidadUpdateComponent,
    resolve: {
      funcionalidad: FuncionalidadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default funcionalidadRoute;
