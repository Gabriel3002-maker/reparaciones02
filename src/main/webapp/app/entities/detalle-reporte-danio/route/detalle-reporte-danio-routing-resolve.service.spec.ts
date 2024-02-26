import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IDetalleReporteDanio } from '../detalle-reporte-danio.model';
import { DetalleReporteDanioService } from '../service/detalle-reporte-danio.service';

import detalleReporteDanioResolve from './detalle-reporte-danio-routing-resolve.service';

describe('DetalleReporteDanio routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: DetalleReporteDanioService;
  let resultDetalleReporteDanio: IDetalleReporteDanio | null | undefined;

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
    service = TestBed.inject(DetalleReporteDanioService);
    resultDetalleReporteDanio = undefined;
  });

  describe('resolve', () => {
    it('should return IDetalleReporteDanio returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        detalleReporteDanioResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultDetalleReporteDanio = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDetalleReporteDanio).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        detalleReporteDanioResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultDetalleReporteDanio = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDetalleReporteDanio).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IDetalleReporteDanio>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        detalleReporteDanioResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultDetalleReporteDanio = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDetalleReporteDanio).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
