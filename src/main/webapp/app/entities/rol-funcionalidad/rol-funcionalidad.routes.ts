import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RolFuncionalidadComponent } from './list/rol-funcionalidad.component';
import { RolFuncionalidadDetailComponent } from './detail/rol-funcionalidad-detail.component';
import { RolFuncionalidadUpdateComponent } from './update/rol-funcionalidad-update.component';
import RolFuncionalidadResolve from './route/rol-funcionalidad-routing-resolve.service';

const rolFuncionalidadRoute: Routes = [
  {
    path: '',
    component: RolFuncionalidadComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RolFuncionalidadDetailComponent,
    resolve: {
      rolFuncionalidad: RolFuncionalidadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RolFuncionalidadUpdateComponent,
    resolve: {
      rolFuncionalidad: RolFuncionalidadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RolFuncionalidadUpdateComponent,
    resolve: {
      rolFuncionalidad: RolFuncionalidadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default rolFuncionalidadRoute;
