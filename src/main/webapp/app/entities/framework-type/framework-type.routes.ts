import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FrameworkTypeComponent } from './list/framework-type.component';
import { FrameworkTypeDetailComponent } from './detail/framework-type-detail.component';
import { FrameworkTypeUpdateComponent } from './update/framework-type-update.component';
import FrameworkTypeResolve from './route/framework-type-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const frameworkTypeRoute: Routes = [
  {
    path: '',
    component: FrameworkTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FrameworkTypeDetailComponent,
    resolve: {
      frameworkType: FrameworkTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FrameworkTypeUpdateComponent,
    resolve: {
      frameworkType: FrameworkTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FrameworkTypeUpdateComponent,
    resolve: {
      frameworkType: FrameworkTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default frameworkTypeRoute;
