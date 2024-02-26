import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICatalogoItem, NewCatalogoItem } from '../catalogo-item.model';

export type PartialUpdateCatalogoItem = Partial<ICatalogoItem> & Pick<ICatalogoItem, 'id'>;

export type EntityResponseType = HttpResponse<ICatalogoItem>;
export type EntityArrayResponseType = HttpResponse<ICatalogoItem[]>;

@Injectable({ providedIn: 'root' })
export class CatalogoItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/catalogo-items');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(catalogoItem: NewCatalogoItem): Observable<EntityResponseType> {
    return this.http.post<ICatalogoItem>(this.resourceUrl, catalogoItem, { observe: 'response' });
  }

  update(catalogoItem: ICatalogoItem): Observable<EntityResponseType> {
    return this.http.put<ICatalogoItem>(`${this.resourceUrl}/${this.getCatalogoItemIdentifier(catalogoItem)}`, catalogoItem, {
      observe: 'response',
    });
  }

  partialUpdate(catalogoItem: PartialUpdateCatalogoItem): Observable<EntityResponseType> {
    return this.http.patch<ICatalogoItem>(`${this.resourceUrl}/${this.getCatalogoItemIdentifier(catalogoItem)}`, catalogoItem, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICatalogoItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICatalogoItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCatalogoItemIdentifier(catalogoItem: Pick<ICatalogoItem, 'id'>): number {
    return catalogoItem.id;
  }

  compareCatalogoItem(o1: Pick<ICatalogoItem, 'id'> | null, o2: Pick<ICatalogoItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getCatalogoItemIdentifier(o1) === this.getCatalogoItemIdentifier(o2) : o1 === o2;
  }

  addCatalogoItemToCollectionIfMissing<Type extends Pick<ICatalogoItem, 'id'>>(
    catalogoItemCollection: Type[],
    ...catalogoItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const catalogoItems: Type[] = catalogoItemsToCheck.filter(isPresent);
    if (catalogoItems.length > 0) {
      const catalogoItemCollectionIdentifiers = catalogoItemCollection.map(
        catalogoItemItem => this.getCatalogoItemIdentifier(catalogoItemItem)!,
      );
      const catalogoItemsToAdd = catalogoItems.filter(catalogoItemItem => {
        const catalogoItemIdentifier = this.getCatalogoItemIdentifier(catalogoItemItem);
        if (catalogoItemCollectionIdentifiers.includes(catalogoItemIdentifier)) {
          return false;
        }
        catalogoItemCollectionIdentifiers.push(catalogoItemIdentifier);
        return true;
      });
      return [...catalogoItemsToAdd, ...catalogoItemCollection];
    }
    return catalogoItemCollection;
  }
}
