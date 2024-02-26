import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OrdenBodegaService } from '../service/orden-bodega.service';

import { OrdenBodegaComponent } from './orden-bodega.component';

describe('OrdenBodega Management Component', () => {
  let comp: OrdenBodegaComponent;
  let fixture: ComponentFixture<OrdenBodegaComponent>;
  let service: OrdenBodegaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'orden-bodega', component: OrdenBodegaComponent }]),
        HttpClientTestingModule,
        OrdenBodegaComponent,
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
      .overrideTemplate(OrdenBodegaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrdenBodegaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OrdenBodegaService);

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
    expect(comp.ordenBodegas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to ordenBodegaService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getOrdenBodegaIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getOrdenBodegaIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
