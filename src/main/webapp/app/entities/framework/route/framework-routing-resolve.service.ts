import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFramework } from '../framework.model';
import { FrameworkService } from '../service/framework.service';

export const frameworkResolve = (route: ActivatedRouteSnapshot): Observable<null | IFramework> => {
  const id = route.params['id'];
  if (id) {
    return inject(FrameworkService)
      .find(id)
      .pipe(
        mergeMap((framework: HttpResponse<IFramework>) => {
          if (framework.body) {
            return of(framework.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default frameworkResolve;
