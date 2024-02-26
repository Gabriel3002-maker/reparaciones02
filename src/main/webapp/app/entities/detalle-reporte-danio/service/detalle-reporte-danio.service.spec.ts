import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IDetalleReporteDanio } from '../detalle-reporte-danio.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../detalle-reporte-danio.test-samples';

import { DetalleReporteDanioService, RestDetalleReporteDanio } from './detalle-reporte-danio.service';

const requireRestSample: RestDetalleReporteDanio = {
  ...sampleWithRequiredData,
  fecha: sampleWithRequiredData.fecha?.format(DATE_FORMAT),
};

describe('DetalleReporteDanio Service', () => {
  let service: DetalleReporteDanioService;
  let httpMock: HttpTestingController;
  let expectedResult: IDetalleReporteDanio | IDetalleReporteDanio[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DetalleReporteDanioService);
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

    it('should create a DetalleReporteDanio', () => {
      const detalleReporteDanio = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(detalleReporteDanio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DetalleReporteDanio', () => {
      const detalleReporteDanio = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(detalleReporteDanio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DetalleReporteDanio', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DetalleReporteDanio', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DetalleReporteDanio', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDetalleReporteDanioToCollectionIfMissing', () => {
      it('should add a DetalleReporteDanio to an empty array', () => {
        const detalleReporteDanio: IDetalleReporteDanio = sampleWithRequiredData;
        expectedResult = service.addDetalleReporteDanioToCollectionIfMissing([], detalleReporteDanio);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(detalleReporteDanio);
      });

      it('should not add a DetalleReporteDanio to an array that contains it', () => {
        const detalleReporteDanio: IDetalleReporteDanio = sampleWithRequiredData;
        const detalleReporteDanioCollection: IDetalleReporteDanio[] = [
          {
            ...detalleReporteDanio,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDetalleReporteDanioToCollectionIfMissing(detalleReporteDanioCollection, detalleReporteDanio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DetalleReporteDanio to an array that doesn't contain it", () => {
        const detalleReporteDanio: IDetalleReporteDanio = sampleWithRequiredData;
        const detalleReporteDanioCollection: IDetalleReporteDanio[] = [sampleWithPartialData];
        expectedResult = service.addDetalleReporteDanioToCollectionIfMissing(detalleReporteDanioCollection, detalleReporteDanio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(detalleReporteDanio);
      });

      it('should add only unique DetalleReporteDanio to an array', () => {
        const detalleReporteDanioArray: IDetalleReporteDanio[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const detalleReporteDanioCollection: IDetalleReporteDanio[] = [sampleWithRequiredData];
        expectedResult = service.addDetalleReporteDanioToCollectionIfMissing(detalleReporteDanioCollection, ...detalleReporteDanioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const detalleReporteDanio: IDetalleReporteDanio = sampleWithRequiredData;
        const detalleReporteDanio2: IDetalleReporteDanio = sampleWithPartialData;
        expectedResult = service.addDetalleReporteDanioToCollectionIfMissing([], detalleReporteDanio, detalleReporteDanio2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(detalleReporteDanio);
        expect(expectedResult).toContain(detalleReporteDanio2);
      });

      it('should accept null and undefined values', () => {
        const detalleReporteDanio: IDetalleReporteDanio = sampleWithRequiredData;
        expectedResult = service.addDetalleReporteDanioToCollectionIfMissing([], null, detalleReporteDanio, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(detalleReporteDanio);
      });

      it('should return initial array if no DetalleReporteDanio is added', () => {
        const detalleReporteDanioCollection: IDetalleReporteDanio[] = [sampleWithRequiredData];
        expectedResult = service.addDetalleReporteDanioToCollectionIfMissing(detalleReporteDanioCollection, undefined, null);
        expect(expectedResult).toEqual(detalleReporteDanioCollection);
      });
    });

    describe('compareDetalleReporteDanio', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDetalleReporteDanio(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDetalleReporteDanio(entity1, entity2);
        const compareResult2 = service.compareDetalleReporteDanio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDetalleReporteDanio(entity1, entity2);
        const compareResult2 = service.compareDetalleReporteDanio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDetalleReporteDanio(entity1, entity2);
        const compareResult2 = service.compareDetalleReporteDanio(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
