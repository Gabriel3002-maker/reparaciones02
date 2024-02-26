import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMaterialReporteControl } from '../material-reporte-control.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../material-reporte-control.test-samples';

import { MaterialReporteControlService } from './material-reporte-control.service';

const requireRestSample: IMaterialReporteControl = {
  ...sampleWithRequiredData,
};

describe('MaterialReporteControl Service', () => {
  let service: MaterialReporteControlService;
  let httpMock: HttpTestingController;
  let expectedResult: IMaterialReporteControl | IMaterialReporteControl[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MaterialReporteControlService);
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

    it('should create a MaterialReporteControl', () => {
      const materialReporteControl = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(materialReporteControl).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MaterialReporteControl', () => {
      const materialReporteControl = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(materialReporteControl).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MaterialReporteControl', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MaterialReporteControl', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MaterialReporteControl', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMaterialReporteControlToCollectionIfMissing', () => {
      it('should add a MaterialReporteControl to an empty array', () => {
        const materialReporteControl: IMaterialReporteControl = sampleWithRequiredData;
        expectedResult = service.addMaterialReporteControlToCollectionIfMissing([], materialReporteControl);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(materialReporteControl);
      });

      it('should not add a MaterialReporteControl to an array that contains it', () => {
        const materialReporteControl: IMaterialReporteControl = sampleWithRequiredData;
        const materialReporteControlCollection: IMaterialReporteControl[] = [
          {
            ...materialReporteControl,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMaterialReporteControlToCollectionIfMissing(materialReporteControlCollection, materialReporteControl);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MaterialReporteControl to an array that doesn't contain it", () => {
        const materialReporteControl: IMaterialReporteControl = sampleWithRequiredData;
        const materialReporteControlCollection: IMaterialReporteControl[] = [sampleWithPartialData];
        expectedResult = service.addMaterialReporteControlToCollectionIfMissing(materialReporteControlCollection, materialReporteControl);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(materialReporteControl);
      });

      it('should add only unique MaterialReporteControl to an array', () => {
        const materialReporteControlArray: IMaterialReporteControl[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const materialReporteControlCollection: IMaterialReporteControl[] = [sampleWithRequiredData];
        expectedResult = service.addMaterialReporteControlToCollectionIfMissing(
          materialReporteControlCollection,
          ...materialReporteControlArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const materialReporteControl: IMaterialReporteControl = sampleWithRequiredData;
        const materialReporteControl2: IMaterialReporteControl = sampleWithPartialData;
        expectedResult = service.addMaterialReporteControlToCollectionIfMissing([], materialReporteControl, materialReporteControl2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(materialReporteControl);
        expect(expectedResult).toContain(materialReporteControl2);
      });

      it('should accept null and undefined values', () => {
        const materialReporteControl: IMaterialReporteControl = sampleWithRequiredData;
        expectedResult = service.addMaterialReporteControlToCollectionIfMissing([], null, materialReporteControl, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(materialReporteControl);
      });

      it('should return initial array if no MaterialReporteControl is added', () => {
        const materialReporteControlCollection: IMaterialReporteControl[] = [sampleWithRequiredData];
        expectedResult = service.addMaterialReporteControlToCollectionIfMissing(materialReporteControlCollection, undefined, null);
        expect(expectedResult).toEqual(materialReporteControlCollection);
      });
    });

    describe('compareMaterialReporteControl', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMaterialReporteControl(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMaterialReporteControl(entity1, entity2);
        const compareResult2 = service.compareMaterialReporteControl(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMaterialReporteControl(entity1, entity2);
        const compareResult2 = service.compareMaterialReporteControl(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMaterialReporteControl(entity1, entity2);
        const compareResult2 = service.compareMaterialReporteControl(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
