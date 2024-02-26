import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRegistroDanio, NewRegistroDanio } from '../registro-danio.model';

export type PartialUpdateRegistroDanio = Partial<IRegistroDanio> & Pick<IRegistroDanio, 'id'>;

type RestOf<T extends IRegistroDanio | NewRegistroDanio> = Omit<T, 'fecha' | 'fechaInicio' | 'fechaFin'> & {
  fecha?: string | null;
  fechaInicio?: string | null;
  fechaFin?: string | null;
};

export type RestRegistroDanio = RestOf<IRegistroDanio>;

export type NewRestRegistroDanio = RestOf<NewRegistroDanio>;

export type PartialUpdateRestRegistroDanio = RestOf<PartialUpdateRegistroDanio>;

export type EntityResponseType = HttpResponse<IRegistroDanio>;
export type EntityArrayResponseType = HttpResponse<IRegistroDanio[]>;

@Injectable({ providedIn: 'root' })
export class RegistroDanioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/registro-danios');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(registroDanio: NewRegistroDanio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registroDanio);
    return this.http
      .post<RestRegistroDanio>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(registroDanio: IRegistroDanio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registroDanio);
    return this.http
      .put<RestRegistroDanio>(`${this.resourceUrl}/${this.getRegistroDanioIdentifier(registroDanio)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(registroDanio: PartialUpdateRegistroDanio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registroDanio);
    return this.http
      .patch<RestRegistroDanio>(`${this.resourceUrl}/${this.getRegistroDanioIdentifier(registroDanio)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRegistroDanio>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRegistroDanio[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRegistroDanioIdentifier(registroDanio: Pick<IRegistroDanio, 'id'>): number {
    return registroDanio.id;
  }

  compareRegistroDanio(o1: Pick<IRegistroDanio, 'id'> | null, o2: Pick<IRegistroDanio, 'id'> | null): boolean {
    return o1 && o2 ? this.getRegistroDanioIdentifier(o1) === this.getRegistroDanioIdentifier(o2) : o1 === o2;
  }

  addRegistroDanioToCollectionIfMissing<Type extends Pick<IRegistroDanio, 'id'>>(
    registroDanioCollection: Type[],
    ...registroDaniosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const registroDanios: Type[] = registroDaniosToCheck.filter(isPresent);
    if (registroDanios.length > 0) {
      const registroDanioCollectionIdentifiers = registroDanioCollection.map(
        registroDanioItem => this.getRegistroDanioIdentifier(registroDanioItem)!,
      );
      const registroDaniosToAdd = registroDanios.filter(registroDanioItem => {
        const registroDanioIdentifier = this.getRegistroDanioIdentifier(registroDanioItem);
        if (registroDanioCollectionIdentifiers.includes(registroDanioIdentifier)) {
          return false;
        }
        registroDanioCollectionIdentifiers.push(registroDanioIdentifier);
        return true;
      });
      return [...registroDaniosToAdd, ...registroDanioCollection];
    }
    return registroDanioCollection;
  }

  protected convertDateFromClient<T extends IRegistroDanio | NewRegistroDanio | PartialUpdateRegistroDanio>(registroDanio: T): RestOf<T> {
    return {
      ...registroDanio,
      fecha: registroDanio.fecha?.format(DATE_FORMAT) ?? null,
      fechaInicio: registroDanio.fechaInicio?.format(DATE_FORMAT) ?? null,
      fechaFin: registroDanio.fechaFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restRegistroDanio: RestRegistroDanio): IRegistroDanio {
    return {
      ...restRegistroDanio,
      fecha: restRegistroDanio.fecha ? dayjs(restRegistroDanio.fecha) : undefined,
      fechaInicio: restRegistroDanio.fechaInicio ? dayjs(restRegistroDanio.fechaInicio) : undefined,
      fechaFin: restRegistroDanio.fechaFin ? dayjs(restRegistroDanio.fechaFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRegistroDanio>): HttpResponse<IRegistroDanio> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRegistroDanio[]>): HttpResponse<IRegistroDanio[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
