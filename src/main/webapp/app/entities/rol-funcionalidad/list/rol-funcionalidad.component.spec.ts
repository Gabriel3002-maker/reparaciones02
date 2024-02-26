import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RolFuncionalidadService } from '../service/rol-funcionalidad.service';

import { RolFuncionalidadComponent } from './rol-funcionalidad.component';

describe('RolFuncionalidad Management Component', () => {
  let comp: RolFuncionalidadComponent;
  let fixture: ComponentFixture<RolFuncionalidadComponent>;
  let service: RolFuncionalidadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'rol-funcionalidad', component: RolFuncionalidadComponent }]),
        HttpClientTestingModule,
        RolFuncionalidadComponent,
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
      .overrideTemplate(RolFuncionalidadComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RolFuncionalidadComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RolFuncionalidadService);

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
    expect(comp.rolFuncionalidads?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to rolFuncionalidadService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getRolFuncionalidadIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getRolFuncionalidadIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
