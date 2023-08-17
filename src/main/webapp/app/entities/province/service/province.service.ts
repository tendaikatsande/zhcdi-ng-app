import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProvince, NewProvince } from '../province.model';

export type PartialUpdateProvince = Partial<IProvince> & Pick<IProvince, 'id'>;

type RestOf<T extends IProvince | NewProvince> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

export type RestProvince = RestOf<IProvince>;

export type NewRestProvince = RestOf<NewProvince>;

export type PartialUpdateRestProvince = RestOf<PartialUpdateProvince>;

export type EntityResponseType = HttpResponse<IProvince>;
export type EntityArrayResponseType = HttpResponse<IProvince[]>;

@Injectable({ providedIn: 'root' })
export class ProvinceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/provinces');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(province: NewProvince): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(province);
    return this.http
      .post<RestProvince>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(province: IProvince): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(province);
    return this.http
      .put<RestProvince>(`${this.resourceUrl}/${this.getProvinceIdentifier(province)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(province: PartialUpdateProvince): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(province);
    return this.http
      .patch<RestProvince>(`${this.resourceUrl}/${this.getProvinceIdentifier(province)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProvince>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProvince[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProvinceIdentifier(province: Pick<IProvince, 'id'>): number {
    return province.id;
  }

  compareProvince(o1: Pick<IProvince, 'id'> | null, o2: Pick<IProvince, 'id'> | null): boolean {
    return o1 && o2 ? this.getProvinceIdentifier(o1) === this.getProvinceIdentifier(o2) : o1 === o2;
  }

  addProvinceToCollectionIfMissing<Type extends Pick<IProvince, 'id'>>(
    provinceCollection: Type[],
    ...provincesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const provinces: Type[] = provincesToCheck.filter(isPresent);
    if (provinces.length > 0) {
      const provinceCollectionIdentifiers = provinceCollection.map(provinceItem => this.getProvinceIdentifier(provinceItem)!);
      const provincesToAdd = provinces.filter(provinceItem => {
        const provinceIdentifier = this.getProvinceIdentifier(provinceItem);
        if (provinceCollectionIdentifiers.includes(provinceIdentifier)) {
          return false;
        }
        provinceCollectionIdentifiers.push(provinceIdentifier);
        return true;
      });
      return [...provincesToAdd, ...provinceCollection];
    }
    return provinceCollection;
  }

  protected convertDateFromClient<T extends IProvince | NewProvince | PartialUpdateProvince>(province: T): RestOf<T> {
    return {
      ...province,
      createdDate: province.createdDate?.toJSON() ?? null,
      modifiedDate: province.modifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restProvince: RestProvince): IProvince {
    return {
      ...restProvince,
      createdDate: restProvince.createdDate ? dayjs(restProvince.createdDate) : undefined,
      modifiedDate: restProvince.modifiedDate ? dayjs(restProvince.modifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProvince>): HttpResponse<IProvince> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProvince[]>): HttpResponse<IProvince[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
