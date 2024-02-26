import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IRolFuncionalidad } from '../rol-funcionalidad.model';
import { RolFuncionalidadService } from '../service/rol-funcionalidad.service';

import rolFuncionalidadResolve from './rol-funcionalidad-routing-resolve.service';

describe('RolFuncionalidad routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: RolFuncionalidadService;
  let resultRolFuncionalidad: IRolFuncionalidad | null | undefined;

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
    service = TestBed.inject(RolFuncionalidadService);
    resultRolFuncionalidad = undefined;
  });

  describe('resolve', () => {
    it('should return IRolFuncionalidad returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        rolFuncionalidadResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultRolFuncionalidad = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRolFuncionalidad).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        rolFuncionalidadResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultRolFuncionalidad = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRolFuncionalidad).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IRolFuncionalidad>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        rolFuncionalidadResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultRolFuncionalidad = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRolFuncionalidad).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
