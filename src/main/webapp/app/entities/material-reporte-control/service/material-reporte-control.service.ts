import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMaterialReporteControl, NewMaterialReporteControl } from '../material-reporte-control.model';

export type PartialUpdateMaterialReporteControl = Partial<IMaterialReporteControl> & Pick<IMaterialReporteControl, 'id'>;

export type EntityResponseType = HttpResponse<IMaterialReporteControl>;
export type EntityArrayResponseType = HttpResponse<IMaterialReporteControl[]>;

@Injectable({ providedIn: 'root' })
export class MaterialReporteControlService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/material-reporte-controls');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(materialReporteControl: NewMaterialReporteControl): Observable<EntityResponseType> {
    return this.http.post<IMaterialReporteControl>(this.resourceUrl, materialReporteControl, { observe: 'response' });
  }

  update(materialReporteControl: IMaterialReporteControl): Observable<EntityResponseType> {
    return this.http.put<IMaterialReporteControl>(
      `${this.resourceUrl}/${this.getMaterialReporteControlIdentifier(materialReporteControl)}`,
      materialReporteControl,
      { observe: 'response' },
    );
  }

  partialUpdate(materialReporteControl: PartialUpdateMaterialReporteControl): Observable<EntityResponseType> {
    return this.http.patch<IMaterialReporteControl>(
      `${this.resourceUrl}/${this.getMaterialReporteControlIdentifier(materialReporteControl)}`,
      materialReporteControl,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMaterialReporteControl>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMaterialReporteControl[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMaterialReporteControlIdentifier(materialReporteControl: Pick<IMaterialReporteControl, 'id'>): number {
    return materialReporteControl.id;
  }

  compareMaterialReporteControl(o1: Pick<IMaterialReporteControl, 'id'> | null, o2: Pick<IMaterialReporteControl, 'id'> | null): boolean {
    return o1 && o2 ? this.getMaterialReporteControlIdentifier(o1) === this.getMaterialReporteControlIdentifier(o2) : o1 === o2;
  }

  addMaterialReporteControlToCollectionIfMissing<Type extends Pick<IMaterialReporteControl, 'id'>>(
    materialReporteControlCollection: Type[],
    ...materialReporteControlsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const materialReporteControls: Type[] = materialReporteControlsToCheck.filter(isPresent);
    if (materialReporteControls.length > 0) {
      const materialReporteControlCollectionIdentifiers = materialReporteControlCollection.map(
        materialReporteControlItem => this.getMaterialReporteControlIdentifier(materialReporteControlItem)!,
      );
      const materialReporteControlsToAdd = materialReporteControls.filter(materialReporteControlItem => {
        const materialReporteControlIdentifier = this.getMaterialReporteControlIdentifier(materialReporteControlItem);
        if (materialReporteControlCollectionIdentifiers.includes(materialReporteControlIdentifier)) {
          return false;
        }
        materialReporteControlCollectionIdentifiers.push(materialReporteControlIdentifier);
        return true;
      });
      return [...materialReporteControlsToAdd, ...materialReporteControlCollection];
    }
    return materialReporteControlCollection;
  }
}
