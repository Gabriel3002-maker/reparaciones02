import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IParametroSistema, NewParametroSistema } from '../parametro-sistema.model';

export type PartialUpdateParametroSistema = Partial<IParametroSistema> & Pick<IParametroSistema, 'id'>;

export type EntityResponseType = HttpResponse<IParametroSistema>;
export type EntityArrayResponseType = HttpResponse<IParametroSistema[]>;

@Injectable({ providedIn: 'root' })
export class ParametroSistemaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/parametro-sistemas');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(parametroSistema: NewParametroSistema): Observable<EntityResponseType> {
    return this.http.post<IParametroSistema>(this.resourceUrl, parametroSistema, { observe: 'response' });
  }

  update(parametroSistema: IParametroSistema): Observable<EntityResponseType> {
    return this.http.put<IParametroSistema>(
      `${this.resourceUrl}/${this.getParametroSistemaIdentifier(parametroSistema)}`,
      parametroSistema,
      { observe: 'response' },
    );
  }

  partialUpdate(parametroSistema: PartialUpdateParametroSistema): Observable<EntityResponseType> {
    return this.http.patch<IParametroSistema>(
      `${this.resourceUrl}/${this.getParametroSistemaIdentifier(parametroSistema)}`,
      parametroSistema,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IParametroSistema>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParametroSistema[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getParametroSistemaIdentifier(parametroSistema: Pick<IParametroSistema, 'id'>): number {
    return parametroSistema.id;
  }

  compareParametroSistema(o1: Pick<IParametroSistema, 'id'> | null, o2: Pick<IParametroSistema, 'id'> | null): boolean {
    return o1 && o2 ? this.getParametroSistemaIdentifier(o1) === this.getParametroSistemaIdentifier(o2) : o1 === o2;
  }

  addParametroSistemaToCollectionIfMissing<Type extends Pick<IParametroSistema, 'id'>>(
    parametroSistemaCollection: Type[],
    ...parametroSistemasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const parametroSistemas: Type[] = parametroSistemasToCheck.filter(isPresent);
    if (parametroSistemas.length > 0) {
      const parametroSistemaCollectionIdentifiers = parametroSistemaCollection.map(
        parametroSistemaItem => this.getParametroSistemaIdentifier(parametroSistemaItem)!,
      );
      const parametroSistemasToAdd = parametroSistemas.filter(parametroSistemaItem => {
        const parametroSistemaIdentifier = this.getParametroSistemaIdentifier(parametroSistemaItem);
        if (parametroSistemaCollectionIdentifiers.includes(parametroSistemaIdentifier)) {
          return false;
        }
        parametroSistemaCollectionIdentifiers.push(parametroSistemaIdentifier);
        return true;
      });
      return [...parametroSistemasToAdd, ...parametroSistemaCollection];
    }
    return parametroSistemaCollection;
  }
}
