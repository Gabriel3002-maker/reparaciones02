import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDetalleDanio } from '../detalle-danio.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../detalle-danio.test-samples';

import { DetalleDanioService } from './detalle-danio.service';

const requireRestSample: IDetalleDanio = {
  ...sampleWithRequiredData,
};

describe('DetalleDanio Service', () => {
  let service: DetalleDanioService;
  let httpMock: HttpTestingController;
  let expectedResult: IDetalleDanio | IDetalleDanio[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DetalleDanioService);
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

    it('should create a DetalleDanio', () => {
      const detalleDanio = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(detalleDanio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DetalleDanio', () => {
      const detalleDanio = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(detalleDanio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DetalleDanio', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DetalleDanio', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DetalleDanio', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDetalleDanioToCollectionIfMissing', () => {
      it('should add a DetalleDanio to an empty array', () => {
        const detalleDanio: IDetalleDanio = sampleWithRequiredData;
        expectedResult = service.addDetalleDanioToCollectionIfMissing([], detalleDanio);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(detalleDanio);
      });

      it('should not add a DetalleDanio to an array that contains it', () => {
        const detalleDanio: IDetalleDanio = sampleWithRequiredData;
        const detalleDanioCollection: IDetalleDanio[] = [
          {
            ...detalleDanio,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDetalleDanioToCollectionIfMissing(detalleDanioCollection, detalleDanio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DetalleDanio to an array that doesn't contain it", () => {
        const detalleDanio: IDetalleDanio = sampleWithRequiredData;
        const detalleDanioCollection: IDetalleDanio[] = [sampleWithPartialData];
        expectedResult = service.addDetalleDanioToCollectionIfMissing(detalleDanioCollection, detalleDanio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(detalleDanio);
      });

      it('should add only unique DetalleDanio to an array', () => {
        const detalleDanioArray: IDetalleDanio[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const detalleDanioCollection: IDetalleDanio[] = [sampleWithRequiredData];
        expectedResult = service.addDetalleDanioToCollectionIfMissing(detalleDanioCollection, ...detalleDanioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const detalleDanio: IDetalleDanio = sampleWithRequiredData;
        const detalleDanio2: IDetalleDanio = sampleWithPartialData;
        expectedResult = service.addDetalleDanioToCollectionIfMissing([], detalleDanio, detalleDanio2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(detalleDanio);
        expect(expectedResult).toContain(detalleDanio2);
      });

      it('should accept null and undefined values', () => {
        const detalleDanio: IDetalleDanio = sampleWithRequiredData;
        expectedResult = service.addDetalleDanioToCollectionIfMissing([], null, detalleDanio, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(detalleDanio);
      });

      it('should return initial array if no DetalleDanio is added', () => {
        const detalleDanioCollection: IDetalleDanio[] = [sampleWithRequiredData];
        expectedResult = service.addDetalleDanioToCollectionIfMissing(detalleDanioCollection, undefined, null);
        expect(expectedResult).toEqual(detalleDanioCollection);
      });
    });

    describe('compareDetalleDanio', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDetalleDanio(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDetalleDanio(entity1, entity2);
        const compareResult2 = service.compareDetalleDanio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDetalleDanio(entity1, entity2);
        const compareResult2 = service.compareDetalleDanio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDetalleDanio(entity1, entity2);
        const compareResult2 = service.compareDetalleDanio(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
