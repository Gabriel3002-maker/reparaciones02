import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IRegistroDanio } from '../registro-danio.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../registro-danio.test-samples';

import { RegistroDanioService, RestRegistroDanio } from './registro-danio.service';

const requireRestSample: RestRegistroDanio = {
  ...sampleWithRequiredData,
  fecha: sampleWithRequiredData.fecha?.format(DATE_FORMAT),
  fechaInicio: sampleWithRequiredData.fechaInicio?.format(DATE_FORMAT),
  fechaFin: sampleWithRequiredData.fechaFin?.format(DATE_FORMAT),
};

describe('RegistroDanio Service', () => {
  let service: RegistroDanioService;
  let httpMock: HttpTestingController;
  let expectedResult: IRegistroDanio | IRegistroDanio[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RegistroDanioService);
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

    it('should create a RegistroDanio', () => {
      const registroDanio = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(registroDanio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RegistroDanio', () => {
      const registroDanio = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(registroDanio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RegistroDanio', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RegistroDanio', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RegistroDanio', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRegistroDanioToCollectionIfMissing', () => {
      it('should add a RegistroDanio to an empty array', () => {
        const registroDanio: IRegistroDanio = sampleWithRequiredData;
        expectedResult = service.addRegistroDanioToCollectionIfMissing([], registroDanio);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(registroDanio);
      });

      it('should not add a RegistroDanio to an array that contains it', () => {
        const registroDanio: IRegistroDanio = sampleWithRequiredData;
        const registroDanioCollection: IRegistroDanio[] = [
          {
            ...registroDanio,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRegistroDanioToCollectionIfMissing(registroDanioCollection, registroDanio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RegistroDanio to an array that doesn't contain it", () => {
        const registroDanio: IRegistroDanio = sampleWithRequiredData;
        const registroDanioCollection: IRegistroDanio[] = [sampleWithPartialData];
        expectedResult = service.addRegistroDanioToCollectionIfMissing(registroDanioCollection, registroDanio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(registroDanio);
      });

      it('should add only unique RegistroDanio to an array', () => {
        const registroDanioArray: IRegistroDanio[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const registroDanioCollection: IRegistroDanio[] = [sampleWithRequiredData];
        expectedResult = service.addRegistroDanioToCollectionIfMissing(registroDanioCollection, ...registroDanioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const registroDanio: IRegistroDanio = sampleWithRequiredData;
        const registroDanio2: IRegistroDanio = sampleWithPartialData;
        expectedResult = service.addRegistroDanioToCollectionIfMissing([], registroDanio, registroDanio2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(registroDanio);
        expect(expectedResult).toContain(registroDanio2);
      });

      it('should accept null and undefined values', () => {
        const registroDanio: IRegistroDanio = sampleWithRequiredData;
        expectedResult = service.addRegistroDanioToCollectionIfMissing([], null, registroDanio, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(registroDanio);
      });

      it('should return initial array if no RegistroDanio is added', () => {
        const registroDanioCollection: IRegistroDanio[] = [sampleWithRequiredData];
        expectedResult = service.addRegistroDanioToCollectionIfMissing(registroDanioCollection, undefined, null);
        expect(expectedResult).toEqual(registroDanioCollection);
      });
    });

    describe('compareRegistroDanio', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRegistroDanio(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRegistroDanio(entity1, entity2);
        const compareResult2 = service.compareRegistroDanio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRegistroDanio(entity1, entity2);
        const compareResult2 = service.compareRegistroDanio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRegistroDanio(entity1, entity2);
        const compareResult2 = service.compareRegistroDanio(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
