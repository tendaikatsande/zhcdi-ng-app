import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FrameworkComponent } from './list/framework.component';
import { FrameworkDetailComponent } from './detail/framework-detail.component';
import { FrameworkUpdateComponent } from './update/framework-update.component';
import FrameworkResolve from './route/framework-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const frameworkRoute: Routes = [
  {
    path: '',
    component: FrameworkComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FrameworkDetailComponent,
    resolve: {
      framework: FrameworkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FrameworkUpdateComponent,
    resolve: {
      framework: FrameworkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FrameworkUpdateComponent,
    resolve: {
      framework: FrameworkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default frameworkRoute;
