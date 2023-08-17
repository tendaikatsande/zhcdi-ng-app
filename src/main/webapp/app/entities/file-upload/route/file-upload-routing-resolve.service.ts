import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFileUpload } from '../file-upload.model';
import { FileUploadService } from '../service/file-upload.service';

export const fileUploadResolve = (route: ActivatedRouteSnapshot): Observable<null | IFileUpload> => {
  const id = route.params['id'];
  if (id) {
    return inject(FileUploadService)
      .find(id)
      .pipe(
        mergeMap((fileUpload: HttpResponse<IFileUpload>) => {
          if (fileUpload.body) {
            return of(fileUpload.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default fileUploadResolve;
