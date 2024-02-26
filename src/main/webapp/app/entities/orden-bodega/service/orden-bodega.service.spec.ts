import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IOrdenBodega } from '../orden-bodega.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../orden-bodega.test-samples';

import { OrdenBodegaService, RestOrdenBodega } from './orden-bodega.service';

const requireRestSample: RestOrdenBodega = {
  ...sampleWithRequiredData,
  fecha: sampleWithRequiredData.fecha?.format(DATE_FORMAT),
};

describe('OrdenBodega Service', () => {
  let service: OrdenBodegaService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrdenBodega | IOrdenBodega[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrdenBodegaService);
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

    it('should create a OrdenBodega', () => {
      const ordenBodega = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ordenBodega).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrdenBodega', () => {
      const ordenBodega = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ordenBodega).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrdenBodega', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrdenBodega', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrdenBodega', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOrdenBodegaToCollectionIfMissing', () => {
      it('should add a OrdenBodega to an empty array', () => {
        const ordenBodega: IOrdenBodega = sampleWithRequiredData;
        expectedResult = service.addOrdenBodegaToCollectionIfMissing([], ordenBodega);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ordenBodega);
      });

      it('should not add a OrdenBodega to an array that contains it', () => {
        const ordenBodega: IOrdenBodega = sampleWithRequiredData;
        const ordenBodegaCollection: IOrdenBodega[] = [
          {
            ...ordenBodega,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrdenBodegaToCollectionIfMissing(ordenBodegaCollection, ordenBodega);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrdenBodega to an array that doesn't contain it", () => {
        const ordenBodega: IOrdenBodega = sampleWithRequiredData;
        const ordenBodegaCollection: IOrdenBodega[] = [sampleWithPartialData];
        expectedResult = service.addOrdenBodegaToCollectionIfMissing(ordenBodegaCollection, ordenBodega);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ordenBodega);
      });

      it('should add only unique OrdenBodega to an array', () => {
        const ordenBodegaArray: IOrdenBodega[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ordenBodegaCollection: IOrdenBodega[] = [sampleWithRequiredData];
        expectedResult = service.addOrdenBodegaToCollectionIfMissing(ordenBodegaCollection, ...ordenBodegaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ordenBodega: IOrdenBodega = sampleWithRequiredData;
        const ordenBodega2: IOrdenBodega = sampleWithPartialData;
        expectedResult = service.addOrdenBodegaToCollectionIfMissing([], ordenBodega, ordenBodega2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ordenBodega);
        expect(expectedResult).toContain(ordenBodega2);
      });

      it('should accept null and undefined values', () => {
        const ordenBodega: IOrdenBodega = sampleWithRequiredData;
        expectedResult = service.addOrdenBodegaToCollectionIfMissing([], null, ordenBodega, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ordenBodega);
      });

      it('should return initial array if no OrdenBodega is added', () => {
        const ordenBodegaCollection: IOrdenBodega[] = [sampleWithRequiredData];
        expectedResult = service.addOrdenBodegaToCollectionIfMissing(ordenBodegaCollection, undefined, null);
        expect(expectedResult).toEqual(ordenBodegaCollection);
      });
    });

    describe('compareOrdenBodega', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrdenBodega(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOrdenBodega(entity1, entity2);
        const compareResult2 = service.compareOrdenBodega(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOrdenBodega(entity1, entity2);
        const compareResult2 = service.compareOrdenBodega(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOrdenBodega(entity1, entity2);
        const compareResult2 = service.compareOrdenBodega(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
