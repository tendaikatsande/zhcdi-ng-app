import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFrameworkType } from '../framework-type.model';
import { FrameworkTypeService } from '../service/framework-type.service';

export const frameworkTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IFrameworkType> => {
  const id = route.params['id'];
  if (id) {
    return inject(FrameworkTypeService)
      .find(id)
      .pipe(
        mergeMap((frameworkType: HttpResponse<IFrameworkType>) => {
          if (frameworkType.body) {
            return of(frameworkType.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default frameworkTypeResolve;
