import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFramework, NewFramework } from '../framework.model';

export type PartialUpdateFramework = Partial<IFramework> & Pick<IFramework, 'id'>;

type RestOf<T extends IFramework | NewFramework> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

export type RestFramework = RestOf<IFramework>;

export type NewRestFramework = RestOf<NewFramework>;

export type PartialUpdateRestFramework = RestOf<PartialUpdateFramework>;

export type EntityResponseType = HttpResponse<IFramework>;
export type EntityArrayResponseType = HttpResponse<IFramework[]>;

@Injectable({ providedIn: 'root' })
export class FrameworkService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/frameworks');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(framework: NewFramework): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(framework);
    return this.http
      .post<RestFramework>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(framework: IFramework): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(framework);
    return this.http
      .put<RestFramework>(`${this.resourceUrl}/${this.getFrameworkIdentifier(framework)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(framework: PartialUpdateFramework): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(framework);
    return this.http
      .patch<RestFramework>(`${this.resourceUrl}/${this.getFrameworkIdentifier(framework)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFramework>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFramework[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFrameworkIdentifier(framework: Pick<IFramework, 'id'>): number {
    return framework.id;
  }

  compareFramework(o1: Pick<IFramework, 'id'> | null, o2: Pick<IFramework, 'id'> | null): boolean {
    return o1 && o2 ? this.getFrameworkIdentifier(o1) === this.getFrameworkIdentifier(o2) : o1 === o2;
  }

  addFrameworkToCollectionIfMissing<Type extends Pick<IFramework, 'id'>>(
    frameworkCollection: Type[],
    ...frameworksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const frameworks: Type[] = frameworksToCheck.filter(isPresent);
    if (frameworks.length > 0) {
      const frameworkCollectionIdentifiers = frameworkCollection.map(frameworkItem => this.getFrameworkIdentifier(frameworkItem)!);
      const frameworksToAdd = frameworks.filter(frameworkItem => {
        const frameworkIdentifier = this.getFrameworkIdentifier(frameworkItem);
        if (frameworkCollectionIdentifiers.includes(frameworkIdentifier)) {
          return false;
        }
        frameworkCollectionIdentifiers.push(frameworkIdentifier);
        return true;
      });
      return [...frameworksToAdd, ...frameworkCollection];
    }
    return frameworkCollection;
  }

  protected convertDateFromClient<T extends IFramework | NewFramework | PartialUpdateFramework>(framework: T): RestOf<T> {
    return {
      ...framework,
      createdDate: framework.createdDate?.toJSON() ?? null,
      modifiedDate: framework.modifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFramework: RestFramework): IFramework {
    return {
      ...restFramework,
      createdDate: restFramework.createdDate ? dayjs(restFramework.createdDate) : undefined,
      modifiedDate: restFramework.modifiedDate ? dayjs(restFramework.modifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFramework>): HttpResponse<IFramework> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFramework[]>): HttpResponse<IFramework[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
