import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CsoComponent } from './list/cso.component';
import { CsoDetailComponent } from './detail/cso-detail.component';
import { CsoUpdateComponent } from './update/cso-update.component';
import CsoResolve from './route/cso-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const csoRoute: Routes = [
  {
    path: '',
    component: CsoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CsoDetailComponent,
    resolve: {
      cso: CsoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CsoUpdateComponent,
    resolve: {
      cso: CsoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CsoUpdateComponent,
    resolve: {
      cso: CsoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default csoRoute;
