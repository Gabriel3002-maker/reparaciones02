import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ParametroSistemaService } from '../service/parametro-sistema.service';

import { ParametroSistemaComponent } from './parametro-sistema.component';

describe('ParametroSistema Management Component', () => {
  let comp: ParametroSistemaComponent;
  let fixture: ComponentFixture<ParametroSistemaComponent>;
  let service: ParametroSistemaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'parametro-sistema', component: ParametroSistemaComponent }]),
        HttpClientTestingModule,
        ParametroSistemaComponent,
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
      .overrideTemplate(ParametroSistemaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ParametroSistemaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ParametroSistemaService);

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
    expect(comp.parametroSistemas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to parametroSistemaService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getParametroSistemaIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getParametroSistemaIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
