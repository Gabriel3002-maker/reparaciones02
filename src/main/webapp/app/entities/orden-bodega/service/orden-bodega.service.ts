import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrdenBodega, NewOrdenBodega } from '../orden-bodega.model';

export type PartialUpdateOrdenBodega = Partial<IOrdenBodega> & Pick<IOrdenBodega, 'id'>;

type RestOf<T extends IOrdenBodega | NewOrdenBodega> = Omit<T, 'fecha'> & {
  fecha?: string | null;
};

export type RestOrdenBodega = RestOf<IOrdenBodega>;

export type NewRestOrdenBodega = RestOf<NewOrdenBodega>;

export type PartialUpdateRestOrdenBodega = RestOf<PartialUpdateOrdenBodega>;

export type EntityResponseType = HttpResponse<IOrdenBodega>;
export type EntityArrayResponseType = HttpResponse<IOrdenBodega[]>;

@Injectable({ providedIn: 'root' })
export class OrdenBodegaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/orden-bodegas');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(ordenBodega: NewOrdenBodega): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ordenBodega);
    return this.http
      .post<RestOrdenBodega>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ordenBodega: IOrdenBodega): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ordenBodega);
    return this.http
      .put<RestOrdenBodega>(`${this.resourceUrl}/${this.getOrdenBodegaIdentifier(ordenBodega)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ordenBodega: PartialUpdateOrdenBodega): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ordenBodega);
    return this.http
      .patch<RestOrdenBodega>(`${this.resourceUrl}/${this.getOrdenBodegaIdentifier(ordenBodega)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOrdenBodega>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOrdenBodega[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrdenBodegaIdentifier(ordenBodega: Pick<IOrdenBodega, 'id'>): number {
    return ordenBodega.id;
  }

  compareOrdenBodega(o1: Pick<IOrdenBodega, 'id'> | null, o2: Pick<IOrdenBodega, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrdenBodegaIdentifier(o1) === this.getOrdenBodegaIdentifier(o2) : o1 === o2;
  }

  addOrdenBodegaToCollectionIfMissing<Type extends Pick<IOrdenBodega, 'id'>>(
    ordenBodegaCollection: Type[],
    ...ordenBodegasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ordenBodegas: Type[] = ordenBodegasToCheck.filter(isPresent);
    if (ordenBodegas.length > 0) {
      const ordenBodegaCollectionIdentifiers = ordenBodegaCollection.map(
        ordenBodegaItem => this.getOrdenBodegaIdentifier(ordenBodegaItem)!,
      );
      const ordenBodegasToAdd = ordenBodegas.filter(ordenBodegaItem => {
        const ordenBodegaIdentifier = this.getOrdenBodegaIdentifier(ordenBodegaItem);
        if (ordenBodegaCollectionIdentifiers.includes(ordenBodegaIdentifier)) {
          return false;
        }
        ordenBodegaCollectionIdentifiers.push(ordenBodegaIdentifier);
        return true;
      });
      return [...ordenBodegasToAdd, ...ordenBodegaCollection];
    }
    return ordenBodegaCollection;
  }

  protected convertDateFromClient<T extends IOrdenBodega | NewOrdenBodega | PartialUpdateOrdenBodega>(ordenBodega: T): RestOf<T> {
    return {
      ...ordenBodega,
      fecha: ordenBodega.fecha?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restOrdenBodega: RestOrdenBodega): IOrdenBodega {
    return {
      ...restOrdenBodega,
      fecha: restOrdenBodega.fecha ? dayjs(restOrdenBodega.fecha) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOrdenBodega>): HttpResponse<IOrdenBodega> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOrdenBodega[]>): HttpResponse<IOrdenBodega[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
