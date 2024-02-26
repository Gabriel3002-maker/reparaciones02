import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMaterialDanio } from '../material-danio.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../material-danio.test-samples';

import { MaterialDanioService } from './material-danio.service';

const requireRestSample: IMaterialDanio = {
  ...sampleWithRequiredData,
};

describe('MaterialDanio Service', () => {
  let service: MaterialDanioService;
  let httpMock: HttpTestingController;
  let expectedResult: IMaterialDanio | IMaterialDanio[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MaterialDanioService);
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

    it('should create a MaterialDanio', () => {
      const materialDanio = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(materialDanio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MaterialDanio', () => {
      const materialDanio = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(materialDanio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MaterialDanio', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MaterialDanio', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MaterialDanio', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMaterialDanioToCollectionIfMissing', () => {
      it('should add a MaterialDanio to an empty array', () => {
        const materialDanio: IMaterialDanio = sampleWithRequiredData;
        expectedResult = service.addMaterialDanioToCollectionIfMissing([], materialDanio);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(materialDanio);
      });

      it('should not add a MaterialDanio to an array that contains it', () => {
        const materialDanio: IMaterialDanio = sampleWithRequiredData;
        const materialDanioCollection: IMaterialDanio[] = [
          {
            ...materialDanio,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMaterialDanioToCollectionIfMissing(materialDanioCollection, materialDanio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MaterialDanio to an array that doesn't contain it", () => {
        const materialDanio: IMaterialDanio = sampleWithRequiredData;
        const materialDanioCollection: IMaterialDanio[] = [sampleWithPartialData];
        expectedResult = service.addMaterialDanioToCollectionIfMissing(materialDanioCollection, materialDanio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(materialDanio);
      });

      it('should add only unique MaterialDanio to an array', () => {
        const materialDanioArray: IMaterialDanio[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const materialDanioCollection: IMaterialDanio[] = [sampleWithRequiredData];
        expectedResult = service.addMaterialDanioToCollectionIfMissing(materialDanioCollection, ...materialDanioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const materialDanio: IMaterialDanio = sampleWithRequiredData;
        const materialDanio2: IMaterialDanio = sampleWithPartialData;
        expectedResult = service.addMaterialDanioToCollectionIfMissing([], materialDanio, materialDanio2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(materialDanio);
        expect(expectedResult).toContain(materialDanio2);
      });

      it('should accept null and undefined values', () => {
        const materialDanio: IMaterialDanio = sampleWithRequiredData;
        expectedResult = service.addMaterialDanioToCollectionIfMissing([], null, materialDanio, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(materialDanio);
      });

      it('should return initial array if no MaterialDanio is added', () => {
        const materialDanioCollection: IMaterialDanio[] = [sampleWithRequiredData];
        expectedResult = service.addMaterialDanioToCollectionIfMissing(materialDanioCollection, undefined, null);
        expect(expectedResult).toEqual(materialDanioCollection);
      });
    });

    describe('compareMaterialDanio', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMaterialDanio(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMaterialDanio(entity1, entity2);
        const compareResult2 = service.compareMaterialDanio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMaterialDanio(entity1, entity2);
        const compareResult2 = service.compareMaterialDanio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMaterialDanio(entity1, entity2);
        const compareResult2 = service.compareMaterialDanio(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
