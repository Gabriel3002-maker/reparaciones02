import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MaterialReporteControlService } from '../service/material-reporte-control.service';

import { MaterialReporteControlComponent } from './material-reporte-control.component';

describe('MaterialReporteControl Management Component', () => {
  let comp: MaterialReporteControlComponent;
  let fixture: ComponentFixture<MaterialReporteControlComponent>;
  let service: MaterialReporteControlService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'material-reporte-control', component: MaterialReporteControlComponent }]),
        HttpClientTestingModule,
        MaterialReporteControlComponent,
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
      .overrideTemplate(MaterialReporteControlComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaterialReporteControlComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MaterialReporteControlService);

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
    expect(comp.materialReporteControls?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to materialReporteControlService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getMaterialReporteControlIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getMaterialReporteControlIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
