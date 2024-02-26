import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMachinery } from '../machinery.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../machinery.test-samples';

import { MachineryService } from './machinery.service';

const requireRestSample: IMachinery = {
  ...sampleWithRequiredData,
};

describe('Machinery Service', () => {
  let service: MachineryService;
  let httpMock: HttpTestingController;
  let expectedResult: IMachinery | IMachinery[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MachineryService);
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

    it('should create a Machinery', () => {
      const machinery = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(machinery).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Machinery', () => {
      const machinery = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(machinery).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Machinery', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Machinery', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Machinery', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMachineryToCollectionIfMissing', () => {
      it('should add a Machinery to an empty array', () => {
        const machinery: IMachinery = sampleWithRequiredData;
        expectedResult = service.addMachineryToCollectionIfMissing([], machinery);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(machinery);
      });

      it('should not add a Machinery to an array that contains it', () => {
        const machinery: IMachinery = sampleWithRequiredData;
        const machineryCollection: IMachinery[] = [
          {
            ...machinery,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMachineryToCollectionIfMissing(machineryCollection, machinery);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Machinery to an array that doesn't contain it", () => {
        const machinery: IMachinery = sampleWithRequiredData;
        const machineryCollection: IMachinery[] = [sampleWithPartialData];
        expectedResult = service.addMachineryToCollectionIfMissing(machineryCollection, machinery);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(machinery);
      });

      it('should add only unique Machinery to an array', () => {
        const machineryArray: IMachinery[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const machineryCollection: IMachinery[] = [sampleWithRequiredData];
        expectedResult = service.addMachineryToCollectionIfMissing(machineryCollection, ...machineryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const machinery: IMachinery = sampleWithRequiredData;
        const machinery2: IMachinery = sampleWithPartialData;
        expectedResult = service.addMachineryToCollectionIfMissing([], machinery, machinery2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(machinery);
        expect(expectedResult).toContain(machinery2);
      });

      it('should accept null and undefined values', () => {
        const machinery: IMachinery = sampleWithRequiredData;
        expectedResult = service.addMachineryToCollectionIfMissing([], null, machinery, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(machinery);
      });

      it('should return initial array if no Machinery is added', () => {
        const machineryCollection: IMachinery[] = [sampleWithRequiredData];
        expectedResult = service.addMachineryToCollectionIfMissing(machineryCollection, undefined, null);
        expect(expectedResult).toEqual(machineryCollection);
      });
    });

    describe('compareMachinery', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMachinery(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMachinery(entity1, entity2);
        const compareResult2 = service.compareMachinery(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMachinery(entity1, entity2);
        const compareResult2 = service.compareMachinery(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMachinery(entity1, entity2);
        const compareResult2 = service.compareMachinery(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
