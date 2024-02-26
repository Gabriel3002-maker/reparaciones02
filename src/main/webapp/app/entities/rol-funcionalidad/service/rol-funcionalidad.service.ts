import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRolFuncionalidad, NewRolFuncionalidad } from '../rol-funcionalidad.model';

export type PartialUpdateRolFuncionalidad = Partial<IRolFuncionalidad> & Pick<IRolFuncionalidad, 'id'>;

export type EntityResponseType = HttpResponse<IRolFuncionalidad>;
export type EntityArrayResponseType = HttpResponse<IRolFuncionalidad[]>;

@Injectable({ providedIn: 'root' })
export class RolFuncionalidadService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rol-funcionalidads');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(rolFuncionalidad: NewRolFuncionalidad): Observable<EntityResponseType> {
    return this.http.post<IRolFuncionalidad>(this.resourceUrl, rolFuncionalidad, { observe: 'response' });
  }

  update(rolFuncionalidad: IRolFuncionalidad): Observable<EntityResponseType> {
    return this.http.put<IRolFuncionalidad>(
      `${this.resourceUrl}/${this.getRolFuncionalidadIdentifier(rolFuncionalidad)}`,
      rolFuncionalidad,
      { observe: 'response' },
    );
  }

  partialUpdate(rolFuncionalidad: PartialUpdateRolFuncionalidad): Observable<EntityResponseType> {
    return this.http.patch<IRolFuncionalidad>(
      `${this.resourceUrl}/${this.getRolFuncionalidadIdentifier(rolFuncionalidad)}`,
      rolFuncionalidad,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRolFuncionalidad>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRolFuncionalidad[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRolFuncionalidadIdentifier(rolFuncionalidad: Pick<IRolFuncionalidad, 'id'>): number {
    return rolFuncionalidad.id;
  }

  compareRolFuncionalidad(o1: Pick<IRolFuncionalidad, 'id'> | null, o2: Pick<IRolFuncionalidad, 'id'> | null): boolean {
    return o1 && o2 ? this.getRolFuncionalidadIdentifier(o1) === this.getRolFuncionalidadIdentifier(o2) : o1 === o2;
  }

  addRolFuncionalidadToCollectionIfMissing<Type extends Pick<IRolFuncionalidad, 'id'>>(
    rolFuncionalidadCollection: Type[],
    ...rolFuncionalidadsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const rolFuncionalidads: Type[] = rolFuncionalidadsToCheck.filter(isPresent);
    if (rolFuncionalidads.length > 0) {
      const rolFuncionalidadCollectionIdentifiers = rolFuncionalidadCollection.map(
        rolFuncionalidadItem => this.getRolFuncionalidadIdentifier(rolFuncionalidadItem)!,
      );
      const rolFuncionalidadsToAdd = rolFuncionalidads.filter(rolFuncionalidadItem => {
        const rolFuncionalidadIdentifier = this.getRolFuncionalidadIdentifier(rolFuncionalidadItem);
        if (rolFuncionalidadCollectionIdentifiers.includes(rolFuncionalidadIdentifier)) {
          return false;
        }
        rolFuncionalidadCollectionIdentifiers.push(rolFuncionalidadIdentifier);
        return true;
      });
      return [...rolFuncionalidadsToAdd, ...rolFuncionalidadCollection];
    }
    return rolFuncionalidadCollection;
  }
}
