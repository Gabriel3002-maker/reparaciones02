import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDetalleDanio, NewDetalleDanio } from '../detalle-danio.model';

export type PartialUpdateDetalleDanio = Partial<IDetalleDanio> & Pick<IDetalleDanio, 'id'>;

export type EntityResponseType = HttpResponse<IDetalleDanio>;
export type EntityArrayResponseType = HttpResponse<IDetalleDanio[]>;

@Injectable({ providedIn: 'root' })
export class DetalleDanioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/detalle-danios');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(detalleDanio: NewDetalleDanio): Observable<EntityResponseType> {
    return this.http.post<IDetalleDanio>(this.resourceUrl, detalleDanio, { observe: 'response' });
  }

  update(detalleDanio: IDetalleDanio): Observable<EntityResponseType> {
    return this.http.put<IDetalleDanio>(`${this.resourceUrl}/${this.getDetalleDanioIdentifier(detalleDanio)}`, detalleDanio, {
      observe: 'response',
    });
  }

  partialUpdate(detalleDanio: PartialUpdateDetalleDanio): Observable<EntityResponseType> {
    return this.http.patch<IDetalleDanio>(`${this.resourceUrl}/${this.getDetalleDanioIdentifier(detalleDanio)}`, detalleDanio, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDetalleDanio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDetalleDanio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDetalleDanioIdentifier(detalleDanio: Pick<IDetalleDanio, 'id'>): number {
    return detalleDanio.id;
  }

  compareDetalleDanio(o1: Pick<IDetalleDanio, 'id'> | null, o2: Pick<IDetalleDanio, 'id'> | null): boolean {
    return o1 && o2 ? this.getDetalleDanioIdentifier(o1) === this.getDetalleDanioIdentifier(o2) : o1 === o2;
  }

  addDetalleDanioToCollectionIfMissing<Type extends Pick<IDetalleDanio, 'id'>>(
    detalleDanioCollection: Type[],
    ...detalleDaniosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const detalleDanios: Type[] = detalleDaniosToCheck.filter(isPresent);
    if (detalleDanios.length > 0) {
      const detalleDanioCollectionIdentifiers = detalleDanioCollection.map(
        detalleDanioItem => this.getDetalleDanioIdentifier(detalleDanioItem)!,
      );
      const detalleDaniosToAdd = detalleDanios.filter(detalleDanioItem => {
        const detalleDanioIdentifier = this.getDetalleDanioIdentifier(detalleDanioItem);
        if (detalleDanioCollectionIdentifiers.includes(detalleDanioIdentifier)) {
          return false;
        }
        detalleDanioCollectionIdentifiers.push(detalleDanioIdentifier);
        return true;
      });
      return [...detalleDaniosToAdd, ...detalleDanioCollection];
    }
    return detalleDanioCollection;
  }
}
