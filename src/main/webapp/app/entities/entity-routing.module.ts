import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cso',
        data: { pageTitle: 'zhcdiApplicationApp.cso.home.title' },
        loadChildren: () => import('./cso/cso.routes'),
      },
      {
        path: 'file-upload',
        data: { pageTitle: 'zhcdiApplicationApp.fileUpload.home.title' },
        loadChildren: () => import('./file-upload/file-upload.routes'),
      },
      {
        path: 'framework',
        data: { pageTitle: 'zhcdiApplicationApp.framework.home.title' },
        loadChildren: () => import('./framework/framework.routes'),
      },
      {
        path: 'framework-type',
        data: { pageTitle: 'zhcdiApplicationApp.frameworkType.home.title' },
        loadChildren: () => import('./framework-type/framework-type.routes'),
      },
      {
        path: 'province',
        data: { pageTitle: 'zhcdiApplicationApp.province.home.title' },
        loadChildren: () => import('./province/province.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
