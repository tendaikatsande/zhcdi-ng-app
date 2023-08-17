import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICso } from '../cso.model';
import { CsoService } from '../service/cso.service';

export const csoResolve = (route: ActivatedRouteSnapshot): Observable<null | ICso> => {
  const id = route.params['id'];
  if (id) {
    return inject(CsoService)
      .find(id)
      .pipe(
        mergeMap((cso: HttpResponse<ICso>) => {
          if (cso.body) {
            return of(cso.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default csoResolve;
