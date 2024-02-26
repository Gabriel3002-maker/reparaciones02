import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MaterialReporteControlComponent } from './list/material-reporte-control.component';
import { MaterialReporteControlDetailComponent } from './detail/material-reporte-control-detail.component';
import { MaterialReporteControlUpdateComponent } from './update/material-reporte-control-update.component';
import MaterialReporteControlResolve from './route/material-reporte-control-routing-resolve.service';

const materialReporteControlRoute: Routes = [
  {
    path: '',
    component: MaterialReporteControlComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MaterialReporteControlDetailComponent,
    resolve: {
      materialReporteControl: MaterialReporteControlResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MaterialReporteControlUpdateComponent,
    resolve: {
      materialReporteControl: MaterialReporteControlResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MaterialReporteControlUpdateComponent,
    resolve: {
      materialReporteControl: MaterialReporteControlResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default materialReporteControlRoute;
