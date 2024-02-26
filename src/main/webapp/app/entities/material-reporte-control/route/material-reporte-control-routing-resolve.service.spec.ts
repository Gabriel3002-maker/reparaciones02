import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IMaterialReporteControl } from '../material-reporte-control.model';
import { MaterialReporteControlService } from '../service/material-reporte-control.service';

import materialReporteControlResolve from './material-reporte-control-routing-resolve.service';

describe('MaterialReporteControl routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: MaterialReporteControlService;
  let resultMaterialReporteControl: IMaterialReporteControl | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(MaterialReporteControlService);
    resultMaterialReporteControl = undefined;
  });

  describe('resolve', () => {
    it('should return IMaterialReporteControl returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        materialReporteControlResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultMaterialReporteControl = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMaterialReporteControl).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        materialReporteControlResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultMaterialReporteControl = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultMaterialReporteControl).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IMaterialReporteControl>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        materialReporteControlResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultMaterialReporteControl = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMaterialReporteControl).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
