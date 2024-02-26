import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MachineryComponent } from './list/machinery.component';
import { MachineryDetailComponent } from './detail/machinery-detail.component';
import { MachineryUpdateComponent } from './update/machinery-update.component';
import MachineryResolve from './route/machinery-routing-resolve.service';

const machineryRoute: Routes = [
  {
    path: '',
    component: MachineryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MachineryDetailComponent,
    resolve: {
      machinery: MachineryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MachineryUpdateComponent,
    resolve: {
      machinery: MachineryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MachineryUpdateComponent,
    resolve: {
      machinery: MachineryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default machineryRoute;
