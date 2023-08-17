import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FileUploadComponent } from './list/file-upload.component';
import { FileUploadDetailComponent } from './detail/file-upload-detail.component';
import { FileUploadUpdateComponent } from './update/file-upload-update.component';
import FileUploadResolve from './route/file-upload-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const fileUploadRoute: Routes = [
  {
    path: '',
    component: FileUploadComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FileUploadDetailComponent,
    resolve: {
      fileUpload: FileUploadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FileUploadUpdateComponent,
    resolve: {
      fileUpload: FileUploadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FileUploadUpdateComponent,
    resolve: {
      fileUpload: FileUploadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default fileUploadRoute;
