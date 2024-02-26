import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICatalogoItem } from '../catalogo-item.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../catalogo-item.test-samples';

import { CatalogoItemService } from './catalogo-item.service';

const requireRestSample: ICatalogoItem = {
  ...sampleWithRequiredData,
};

describe('CatalogoItem Service', () => {
  let service: CatalogoItemService;
  let httpMock: HttpTestingController;
  let expectedResult: ICatalogoItem | ICatalogoItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CatalogoItemService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a CatalogoItem', () => {
      const catalogoItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(catalogoItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CatalogoItem', () => {
      const catalogoItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(catalogoItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CatalogoItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CatalogoItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CatalogoItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCatalogoItemToCollectionIfMissing', () => {
      it('should add a CatalogoItem to an empty array', () => {
        const catalogoItem: ICatalogoItem = sampleWithRequiredData;
        expectedResult = service.addCatalogoItemToCollectionIfMissing([], catalogoItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(catalogoItem);
      });

      it('should not add a CatalogoItem to an array that contains it', () => {
        const catalogoItem: ICatalogoItem = sampleWithRequiredData;
        const catalogoItemCollection: ICatalogoItem[] = [
          {
            ...catalogoItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCatalogoItemToCollectionIfMissing(catalogoItemCollection, catalogoItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CatalogoItem to an array that doesn't contain it", () => {
        const catalogoItem: ICatalogoItem = sampleWithRequiredData;
        const catalogoItemCollection: ICatalogoItem[] = [sampleWithPartialData];
        expectedResult = service.addCatalogoItemToCollectionIfMissing(catalogoItemCollection, catalogoItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(catalogoItem);
      });

      it('should add only unique CatalogoItem to an array', () => {
        const catalogoItemArray: ICatalogoItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const catalogoItemCollection: ICatalogoItem[] = [sampleWithRequiredData];
        expectedResult = service.addCatalogoItemToCollectionIfMissing(catalogoItemCollection, ...catalogoItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const catalogoItem: ICatalogoItem = sampleWithRequiredData;
        const catalogoItem2: ICatalogoItem = sampleWithPartialData;
        expectedResult = service.addCatalogoItemToCollectionIfMissing([], catalogoItem, catalogoItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(catalogoItem);
        expect(expectedResult).toContain(catalogoItem2);
      });

      it('should accept null and undefined values', () => {
        const catalogoItem: ICatalogoItem = sampleWithRequiredData;
        expectedResult = service.addCatalogoItemToCollectionIfMissing([], null, catalogoItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(catalogoItem);
      });

      it('should return initial array if no CatalogoItem is added', () => {
        const catalogoItemCollection: ICatalogoItem[] = [sampleWithRequiredData];
        expectedResult = service.addCatalogoItemToCollectionIfMissing(catalogoItemCollection, undefined, null);
        expect(expectedResult).toEqual(catalogoItemCollection);
      });
    });

    describe('compareCatalogoItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCatalogoItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCatalogoItem(entity1, entity2);
        const compareResult2 = service.compareCatalogoItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCatalogoItem(entity1, entity2);
        const compareResult2 = service.compareCatalogoItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCatalogoItem(entity1, entity2);
        const compareResult2 = service.compareCatalogoItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
