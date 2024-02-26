import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMaterialDanio, NewMaterialDanio } from '../material-danio.model';

export type PartialUpdateMaterialDanio = Partial<IMaterialDanio> & Pick<IMaterialDanio, 'id'>;

export type EntityResponseType = HttpResponse<IMaterialDanio>;
export type EntityArrayResponseType = HttpResponse<IMaterialDanio[]>;

@Injectable({ providedIn: 'root' })
export class MaterialDanioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/material-danios');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(materialDanio: NewMaterialDanio): Observable<EntityResponseType> {
    return this.http.post<IMaterialDanio>(this.resourceUrl, materialDanio, { observe: 'response' });
  }

  update(materialDanio: IMaterialDanio): Observable<EntityResponseType> {
    return this.http.put<IMaterialDanio>(`${this.resourceUrl}/${this.getMaterialDanioIdentifier(materialDanio)}`, materialDanio, {
      observe: 'response',
    });
  }

  partialUpdate(materialDanio: PartialUpdateMaterialDanio): Observable<EntityResponseType> {
    return this.http.patch<IMaterialDanio>(`${this.resourceUrl}/${this.getMaterialDanioIdentifier(materialDanio)}`, materialDanio, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMaterialDanio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMaterialDanio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMaterialDanioIdentifier(materialDanio: Pick<IMaterialDanio, 'id'>): number {
    return materialDanio.id;
  }

  compareMaterialDanio(o1: Pick<IMaterialDanio, 'id'> | null, o2: Pick<IMaterialDanio, 'id'> | null): boolean {
    return o1 && o2 ? this.getMaterialDanioIdentifier(o1) === this.getMaterialDanioIdentifier(o2) : o1 === o2;
  }

  addMaterialDanioToCollectionIfMissing<Type extends Pick<IMaterialDanio, 'id'>>(
    materialDanioCollection: Type[],
    ...materialDaniosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const materialDanios: Type[] = materialDaniosToCheck.filter(isPresent);
    if (materialDanios.length > 0) {
      const materialDanioCollectionIdentifiers = materialDanioCollection.map(
        materialDanioItem => this.getMaterialDanioIdentifier(materialDanioItem)!,
      );
      const materialDaniosToAdd = materialDanios.filter(materialDanioItem => {
        const materialDanioIdentifier = this.getMaterialDanioIdentifier(materialDanioItem);
        if (materialDanioCollectionIdentifiers.includes(materialDanioIdentifier)) {
          return false;
        }
        materialDanioCollectionIdentifiers.push(materialDanioIdentifier);
        return true;
      });
      return [...materialDaniosToAdd, ...materialDanioCollection];
    }
    return materialDanioCollection;
  }
}
