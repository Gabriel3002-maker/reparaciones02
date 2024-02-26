import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFuncionalidad, NewFuncionalidad } from '../funcionalidad.model';

export type PartialUpdateFuncionalidad = Partial<IFuncionalidad> & Pick<IFuncionalidad, 'id'>;

export type EntityResponseType = HttpResponse<IFuncionalidad>;
export type EntityArrayResponseType = HttpResponse<IFuncionalidad[]>;

@Injectable({ providedIn: 'root' })
export class FuncionalidadService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/funcionalidads');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(funcionalidad: NewFuncionalidad): Observable<EntityResponseType> {
    return this.http.post<IFuncionalidad>(this.resourceUrl, funcionalidad, { observe: 'response' });
  }

  update(funcionalidad: IFuncionalidad): Observable<EntityResponseType> {
    return this.http.put<IFuncionalidad>(`${this.resourceUrl}/${this.getFuncionalidadIdentifier(funcionalidad)}`, funcionalidad, {
      observe: 'response',
    });
  }

  partialUpdate(funcionalidad: PartialUpdateFuncionalidad): Observable<EntityResponseType> {
    return this.http.patch<IFuncionalidad>(`${this.resourceUrl}/${this.getFuncionalidadIdentifier(funcionalidad)}`, funcionalidad, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFuncionalidad>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFuncionalidad[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFuncionalidadIdentifier(funcionalidad: Pick<IFuncionalidad, 'id'>): number {
    return funcionalidad.id;
  }

  compareFuncionalidad(o1: Pick<IFuncionalidad, 'id'> | null, o2: Pick<IFuncionalidad, 'id'> | null): boolean {
    return o1 && o2 ? this.getFuncionalidadIdentifier(o1) === this.getFuncionalidadIdentifier(o2) : o1 === o2;
  }

  addFuncionalidadToCollectionIfMissing<Type extends Pick<IFuncionalidad, 'id'>>(
    funcionalidadCollection: Type[],
    ...funcionalidadsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const funcionalidads: Type[] = funcionalidadsToCheck.filter(isPresent);
    if (funcionalidads.length > 0) {
      const funcionalidadCollectionIdentifiers = funcionalidadCollection.map(
        funcionalidadItem => this.getFuncionalidadIdentifier(funcionalidadItem)!,
      );
      const funcionalidadsToAdd = funcionalidads.filter(funcionalidadItem => {
        const funcionalidadIdentifier = this.getFuncionalidadIdentifier(funcionalidadItem);
        if (funcionalidadCollectionIdentifiers.includes(funcionalidadIdentifier)) {
          return false;
        }
        funcionalidadCollectionIdentifiers.push(funcionalidadIdentifier);
        return true;
      });
      return [...funcionalidadsToAdd, ...funcionalidadCollection];
    }
    return funcionalidadCollection;
  }
}
