import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDetalleReporteDanio, NewDetalleReporteDanio } from '../detalle-reporte-danio.model';

export type PartialUpdateDetalleReporteDanio = Partial<IDetalleReporteDanio> & Pick<IDetalleReporteDanio, 'id'>;

type RestOf<T extends IDetalleReporteDanio | NewDetalleReporteDanio> = Omit<T, 'fecha'> & {
  fecha?: string | null;
};

export type RestDetalleReporteDanio = RestOf<IDetalleReporteDanio>;

export type NewRestDetalleReporteDanio = RestOf<NewDetalleReporteDanio>;

export type PartialUpdateRestDetalleReporteDanio = RestOf<PartialUpdateDetalleReporteDanio>;

export type EntityResponseType = HttpResponse<IDetalleReporteDanio>;
export type EntityArrayResponseType = HttpResponse<IDetalleReporteDanio[]>;

@Injectable({ providedIn: 'root' })
export class DetalleReporteDanioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/detalle-reporte-danios');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(detalleReporteDanio: NewDetalleReporteDanio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(detalleReporteDanio);
    return this.http
      .post<RestDetalleReporteDanio>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(detalleReporteDanio: IDetalleReporteDanio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(detalleReporteDanio);
    return this.http
      .put<RestDetalleReporteDanio>(`${this.resourceUrl}/${this.getDetalleReporteDanioIdentifier(detalleReporteDanio)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(detalleReporteDanio: PartialUpdateDetalleReporteDanio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(detalleReporteDanio);
    return this.http
      .patch<RestDetalleReporteDanio>(`${this.resourceUrl}/${this.getDetalleReporteDanioIdentifier(detalleReporteDanio)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDetalleReporteDanio>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDetalleReporteDanio[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDetalleReporteDanioIdentifier(detalleReporteDanio: Pick<IDetalleReporteDanio, 'id'>): number {
    return detalleReporteDanio.id;
  }

  compareDetalleReporteDanio(o1: Pick<IDetalleReporteDanio, 'id'> | null, o2: Pick<IDetalleReporteDanio, 'id'> | null): boolean {
    return o1 && o2 ? this.getDetalleReporteDanioIdentifier(o1) === this.getDetalleReporteDanioIdentifier(o2) : o1 === o2;
  }

  addDetalleReporteDanioToCollectionIfMissing<Type extends Pick<IDetalleReporteDanio, 'id'>>(
    detalleReporteDanioCollection: Type[],
    ...detalleReporteDaniosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const detalleReporteDanios: Type[] = detalleReporteDaniosToCheck.filter(isPresent);
    if (detalleReporteDanios.length > 0) {
      const detalleReporteDanioCollectionIdentifiers = detalleReporteDanioCollection.map(
        detalleReporteDanioItem => this.getDetalleReporteDanioIdentifier(detalleReporteDanioItem)!,
      );
      const detalleReporteDaniosToAdd = detalleReporteDanios.filter(detalleReporteDanioItem => {
        const detalleReporteDanioIdentifier = this.getDetalleReporteDanioIdentifier(detalleReporteDanioItem);
        if (detalleReporteDanioCollectionIdentifiers.includes(detalleReporteDanioIdentifier)) {
          return false;
        }
        detalleReporteDanioCollectionIdentifiers.push(detalleReporteDanioIdentifier);
        return true;
      });
      return [...detalleReporteDaniosToAdd, ...detalleReporteDanioCollection];
    }
    return detalleReporteDanioCollection;
  }

  protected convertDateFromClient<T extends IDetalleReporteDanio | NewDetalleReporteDanio | PartialUpdateDetalleReporteDanio>(
    detalleReporteDanio: T,
  ): RestOf<T> {
    return {
      ...detalleReporteDanio,
      fecha: detalleReporteDanio.fecha?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restDetalleReporteDanio: RestDetalleReporteDanio): IDetalleReporteDanio {
    return {
      ...restDetalleReporteDanio,
      fecha: restDetalleReporteDanio.fecha ? dayjs(restDetalleReporteDanio.fecha) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDetalleReporteDanio>): HttpResponse<IDetalleReporteDanio> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDetalleReporteDanio[]>): HttpResponse<IDetalleReporteDanio[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
