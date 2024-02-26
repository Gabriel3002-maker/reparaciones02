import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DetalleReporteDanioComponent } from './list/detalle-reporte-danio.component';
import { DetalleReporteDanioDetailComponent } from './detail/detalle-reporte-danio-detail.component';
import { DetalleReporteDanioUpdateComponent } from './update/detalle-reporte-danio-update.component';
import DetalleReporteDanioResolve from './route/detalle-reporte-danio-routing-resolve.service';

const detalleReporteDanioRoute: Routes = [
  {
    path: '',
    component: DetalleReporteDanioComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DetalleReporteDanioDetailComponent,
    resolve: {
      detalleReporteDanio: DetalleReporteDanioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DetalleReporteDanioUpdateComponent,
    resolve: {
      detalleReporteDanio: DetalleReporteDanioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DetalleReporteDanioUpdateComponent,
    resolve: {
      detalleReporteDanio: DetalleReporteDanioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default detalleReporteDanioRoute;
