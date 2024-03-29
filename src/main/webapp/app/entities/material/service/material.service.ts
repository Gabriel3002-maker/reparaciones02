import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMaterial, NewMaterial } from '../material.model';

export type PartialUpdateMaterial = Partial<IMaterial> & Pick<IMaterial, 'id'>;

type RestOf<T extends IMaterial | NewMaterial> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

export type RestMaterial = RestOf<IMaterial>;

export type NewRestMaterial = RestOf<NewMaterial>;

export type PartialUpdateRestMaterial = RestOf<PartialUpdateMaterial>;

export type EntityResponseType = HttpResponse<IMaterial>;
export type EntityArrayResponseType = HttpResponse<IMaterial[]>;

@Injectable({ providedIn: 'root' })
export class MaterialService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/materials');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(material: NewMaterial): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(material);
    return this.http
      .post<RestMaterial>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(material: IMaterial): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(material);
    return this.http
      .put<RestMaterial>(`${this.resourceUrl}/${this.getMaterialIdentifier(material)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(material: PartialUpdateMaterial): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(material);
    return this.http
      .patch<RestMaterial>(`${this.resourceUrl}/${this.getMaterialIdentifier(material)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMaterial>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMaterial[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMaterialIdentifier(material: Pick<IMaterial, 'id'>): number {
    return material.id;
  }

  compareMaterial(o1: Pick<IMaterial, 'id'> | null, o2: Pick<IMaterial, 'id'> | null): boolean {
    return o1 && o2 ? this.getMaterialIdentifier(o1) === this.getMaterialIdentifier(o2) : o1 === o2;
  }

  addMaterialToCollectionIfMissing<Type extends Pick<IMaterial, 'id'>>(
    materialCollection: Type[],
    ...materialsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const materials: Type[] = materialsToCheck.filter(isPresent);
    if (materials.length > 0) {
      const materialCollectionIdentifiers = materialCollection.map(materialItem => this.getMaterialIdentifier(materialItem)!);
      const materialsToAdd = materials.filter(materialItem => {
        const materialIdentifier = this.getMaterialIdentifier(materialItem);
        if (materialCollectionIdentifiers.includes(materialIdentifier)) {
          return false;
        }
        materialCollectionIdentifiers.push(materialIdentifier);
        return true;
      });
      return [...materialsToAdd, ...materialCollection];
    }
    return materialCollection;
  }

  protected convertDateFromClient<T extends IMaterial | NewMaterial | PartialUpdateMaterial>(material: T): RestOf<T> {
    return {
      ...material,
      fechaCreacion: material.fechaCreacion?.format(DATE_FORMAT) ?? null,
      fechaModificacion: material.fechaModificacion?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restMaterial: RestMaterial): IMaterial {
    return {
      ...restMaterial,
      fechaCreacion: restMaterial.fechaCreacion ? dayjs(restMaterial.fechaCreacion) : undefined,
      fechaModificacion: restMaterial.fechaModificacion ? dayjs(restMaterial.fechaModificacion) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMaterial>): HttpResponse<IMaterial> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMaterial[]>): HttpResponse<IMaterial[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
