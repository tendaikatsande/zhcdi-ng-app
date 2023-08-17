import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFrameworkType, NewFrameworkType } from '../framework-type.model';

export type PartialUpdateFrameworkType = Partial<IFrameworkType> & Pick<IFrameworkType, 'id'>;

type RestOf<T extends IFrameworkType | NewFrameworkType> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

export type RestFrameworkType = RestOf<IFrameworkType>;

export type NewRestFrameworkType = RestOf<NewFrameworkType>;

export type PartialUpdateRestFrameworkType = RestOf<PartialUpdateFrameworkType>;

export type EntityResponseType = HttpResponse<IFrameworkType>;
export type EntityArrayResponseType = HttpResponse<IFrameworkType[]>;

@Injectable({ providedIn: 'root' })
export class FrameworkTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/framework-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(frameworkType: NewFrameworkType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(frameworkType);
    return this.http
      .post<RestFrameworkType>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(frameworkType: IFrameworkType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(frameworkType);
    return this.http
      .put<RestFrameworkType>(`${this.resourceUrl}/${this.getFrameworkTypeIdentifier(frameworkType)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(frameworkType: PartialUpdateFrameworkType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(frameworkType);
    return this.http
      .patch<RestFrameworkType>(`${this.resourceUrl}/${this.getFrameworkTypeIdentifier(frameworkType)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFrameworkType>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFrameworkType[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFrameworkTypeIdentifier(frameworkType: Pick<IFrameworkType, 'id'>): number {
    return frameworkType.id;
  }

  compareFrameworkType(o1: Pick<IFrameworkType, 'id'> | null, o2: Pick<IFrameworkType, 'id'> | null): boolean {
    return o1 && o2 ? this.getFrameworkTypeIdentifier(o1) === this.getFrameworkTypeIdentifier(o2) : o1 === o2;
  }

  addFrameworkTypeToCollectionIfMissing<Type extends Pick<IFrameworkType, 'id'>>(
    frameworkTypeCollection: Type[],
    ...frameworkTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const frameworkTypes: Type[] = frameworkTypesToCheck.filter(isPresent);
    if (frameworkTypes.length > 0) {
      const frameworkTypeCollectionIdentifiers = frameworkTypeCollection.map(
        frameworkTypeItem => this.getFrameworkTypeIdentifier(frameworkTypeItem)!
      );
      const frameworkTypesToAdd = frameworkTypes.filter(frameworkTypeItem => {
        const frameworkTypeIdentifier = this.getFrameworkTypeIdentifier(frameworkTypeItem);
        if (frameworkTypeCollectionIdentifiers.includes(frameworkTypeIdentifier)) {
          return false;
        }
        frameworkTypeCollectionIdentifiers.push(frameworkTypeIdentifier);
        return true;
      });
      return [...frameworkTypesToAdd, ...frameworkTypeCollection];
    }
    return frameworkTypeCollection;
  }

  protected convertDateFromClient<T extends IFrameworkType | NewFrameworkType | PartialUpdateFrameworkType>(frameworkType: T): RestOf<T> {
    return {
      ...frameworkType,
      createdDate: frameworkType.createdDate?.toJSON() ?? null,
      modifiedDate: frameworkType.modifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFrameworkType: RestFrameworkType): IFrameworkType {
    return {
      ...restFrameworkType,
      createdDate: restFrameworkType.createdDate ? dayjs(restFrameworkType.createdDate) : undefined,
      modifiedDate: restFrameworkType.modifiedDate ? dayjs(restFrameworkType.modifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFrameworkType>): HttpResponse<IFrameworkType> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFrameworkType[]>): HttpResponse<IFrameworkType[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
