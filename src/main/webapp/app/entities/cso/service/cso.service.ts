import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICso, NewCso } from '../cso.model';

export type PartialUpdateCso = Partial<ICso> & Pick<ICso, 'id'>;

type RestOf<T extends ICso | NewCso> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

export type RestCso = RestOf<ICso>;

export type NewRestCso = RestOf<NewCso>;

export type PartialUpdateRestCso = RestOf<PartialUpdateCso>;

export type EntityResponseType = HttpResponse<ICso>;
export type EntityArrayResponseType = HttpResponse<ICso[]>;

@Injectable({ providedIn: 'root' })
export class CsoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/csos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cso: NewCso): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cso);
    return this.http.post<RestCso>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cso: ICso): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cso);
    return this.http
      .put<RestCso>(`${this.resourceUrl}/${this.getCsoIdentifier(cso)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cso: PartialUpdateCso): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cso);
    return this.http
      .patch<RestCso>(`${this.resourceUrl}/${this.getCsoIdentifier(cso)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCso>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCso[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCsoIdentifier(cso: Pick<ICso, 'id'>): number {
    return cso.id;
  }

  compareCso(o1: Pick<ICso, 'id'> | null, o2: Pick<ICso, 'id'> | null): boolean {
    return o1 && o2 ? this.getCsoIdentifier(o1) === this.getCsoIdentifier(o2) : o1 === o2;
  }

  addCsoToCollectionIfMissing<Type extends Pick<ICso, 'id'>>(csoCollection: Type[], ...csosToCheck: (Type | null | undefined)[]): Type[] {
    const csos: Type[] = csosToCheck.filter(isPresent);
    if (csos.length > 0) {
      const csoCollectionIdentifiers = csoCollection.map(csoItem => this.getCsoIdentifier(csoItem)!);
      const csosToAdd = csos.filter(csoItem => {
        const csoIdentifier = this.getCsoIdentifier(csoItem);
        if (csoCollectionIdentifiers.includes(csoIdentifier)) {
          return false;
        }
        csoCollectionIdentifiers.push(csoIdentifier);
        return true;
      });
      return [...csosToAdd, ...csoCollection];
    }
    return csoCollection;
  }

  protected convertDateFromClient<T extends ICso | NewCso | PartialUpdateCso>(cso: T): RestOf<T> {
    return {
      ...cso,
      createdDate: cso.createdDate?.toJSON() ?? null,
      modifiedDate: cso.modifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCso: RestCso): ICso {
    return {
      ...restCso,
      createdDate: restCso.createdDate ? dayjs(restCso.createdDate) : undefined,
      modifiedDate: restCso.modifiedDate ? dayjs(restCso.modifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCso>): HttpResponse<ICso> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCso[]>): HttpResponse<ICso[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
