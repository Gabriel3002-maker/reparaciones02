import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IParametroSistema } from '../parametro-sistema.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../parametro-sistema.test-samples';

import { ParametroSistemaService } from './parametro-sistema.service';

const requireRestSample: IParametroSistema = {
  ...sampleWithRequiredData,
};

describe('ParametroSistema Service', () => {
  let service: ParametroSistemaService;
  let httpMock: HttpTestingController;
  let expectedResult: IParametroSistema | IParametroSistema[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ParametroSistemaService);
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

    it('should create a ParametroSistema', () => {
      const parametroSistema = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(parametroSistema).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ParametroSistema', () => {
      const parametroSistema = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(parametroSistema).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ParametroSistema', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ParametroSistema', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ParametroSistema', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addParametroSistemaToCollectionIfMissing', () => {
      it('should add a ParametroSistema to an empty array', () => {
        const parametroSistema: IParametroSistema = sampleWithRequiredData;
        expectedResult = service.addParametroSistemaToCollectionIfMissing([], parametroSistema);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(parametroSistema);
      });

      it('should not add a ParametroSistema to an array that contains it', () => {
        const parametroSistema: IParametroSistema = sampleWithRequiredData;
        const parametroSistemaCollection: IParametroSistema[] = [
          {
            ...parametroSistema,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addParametroSistemaToCollectionIfMissing(parametroSistemaCollection, parametroSistema);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ParametroSistema to an array that doesn't contain it", () => {
        const parametroSistema: IParametroSistema = sampleWithRequiredData;
        const parametroSistemaCollection: IParametroSistema[] = [sampleWithPartialData];
        expectedResult = service.addParametroSistemaToCollectionIfMissing(parametroSistemaCollection, parametroSistema);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(parametroSistema);
      });

      it('should add only unique ParametroSistema to an array', () => {
        const parametroSistemaArray: IParametroSistema[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const parametroSistemaCollection: IParametroSistema[] = [sampleWithRequiredData];
        expectedResult = service.addParametroSistemaToCollectionIfMissing(parametroSistemaCollection, ...parametroSistemaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const parametroSistema: IParametroSistema = sampleWithRequiredData;
        const parametroSistema2: IParametroSistema = sampleWithPartialData;
        expectedResult = service.addParametroSistemaToCollectionIfMissing([], parametroSistema, parametroSistema2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(parametroSistema);
        expect(expectedResult).toContain(parametroSistema2);
      });

      it('should accept null and undefined values', () => {
        const parametroSistema: IParametroSistema = sampleWithRequiredData;
        expectedResult = service.addParametroSistemaToCollectionIfMissing([], null, parametroSistema, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(parametroSistema);
      });

      it('should return initial array if no ParametroSistema is added', () => {
        const parametroSistemaCollection: IParametroSistema[] = [sampleWithRequiredData];
        expectedResult = service.addParametroSistemaToCollectionIfMissing(parametroSistemaCollection, undefined, null);
        expect(expectedResult).toEqual(parametroSistemaCollection);
      });
    });

    describe('compareParametroSistema', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareParametroSistema(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareParametroSistema(entity1, entity2);
        const compareResult2 = service.compareParametroSistema(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareParametroSistema(entity1, entity2);
        const compareResult2 = service.compareParametroSistema(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareParametroSistema(entity1, entity2);
        const compareResult2 = service.compareParametroSistema(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
