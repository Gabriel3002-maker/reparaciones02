import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMachinery, NewMachinery } from '../machinery.model';

export type PartialUpdateMachinery = Partial<IMachinery> & Pick<IMachinery, 'id'>;

export type EntityResponseType = HttpResponse<IMachinery>;
export type EntityArrayResponseType = HttpResponse<IMachinery[]>;

@Injectable({ providedIn: 'root' })
export class MachineryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/machinery');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(machinery: NewMachinery): Observable<EntityResponseType> {
    return this.http.post<IMachinery>(this.resourceUrl, machinery, { observe: 'response' });
  }

  update(machinery: IMachinery): Observable<EntityResponseType> {
    return this.http.put<IMachinery>(`${this.resourceUrl}/${this.getMachineryIdentifier(machinery)}`, machinery, { observe: 'response' });
  }

  partialUpdate(machinery: PartialUpdateMachinery): Observable<EntityResponseType> {
    return this.http.patch<IMachinery>(`${this.resourceUrl}/${this.getMachineryIdentifier(machinery)}`, machinery, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMachinery>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMachinery[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMachineryIdentifier(machinery: Pick<IMachinery, 'id'>): number {
    return machinery.id;
  }

  compareMachinery(o1: Pick<IMachinery, 'id'> | null, o2: Pick<IMachinery, 'id'> | null): boolean {
    return o1 && o2 ? this.getMachineryIdentifier(o1) === this.getMachineryIdentifier(o2) : o1 === o2;
  }

  addMachineryToCollectionIfMissing<Type extends Pick<IMachinery, 'id'>>(
    machineryCollection: Type[],
    ...machineryToCheck: (Type | null | undefined)[]
  ): Type[] {
    const machinery: Type[] = machineryToCheck.filter(isPresent);
    if (machinery.length > 0) {
      const machineryCollectionIdentifiers = machineryCollection.map(machineryItem => this.getMachineryIdentifier(machineryItem)!);
      const machineryToAdd = machinery.filter(machineryItem => {
        const machineryIdentifier = this.getMachineryIdentifier(machineryItem);
        if (machineryCollectionIdentifiers.includes(machineryIdentifier)) {
          return false;
        }
        machineryCollectionIdentifiers.push(machineryIdentifier);
        return true;
      });
      return [...machineryToAdd, ...machineryCollection];
    }
    return machineryCollection;
  }
}
