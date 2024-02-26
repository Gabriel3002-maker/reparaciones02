import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DetalleReporteDanioService } from '../service/detalle-reporte-danio.service';

import { DetalleReporteDanioComponent } from './detalle-reporte-danio.component';

describe('DetalleReporteDanio Management Component', () => {
  let comp: DetalleReporteDanioComponent;
  let fixture: ComponentFixture<DetalleReporteDanioComponent>;
  let service: DetalleReporteDanioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'detalle-reporte-danio', component: DetalleReporteDanioComponent }]),
        HttpClientTestingModule,
        DetalleReporteDanioComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(DetalleReporteDanioComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DetalleReporteDanioComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DetalleReporteDanioService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.detalleReporteDanios?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to detalleReporteDanioService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getDetalleReporteDanioIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getDetalleReporteDanioIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
