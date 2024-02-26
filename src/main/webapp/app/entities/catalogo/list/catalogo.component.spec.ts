import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CatalogoService } from '../service/catalogo.service';

import { CatalogoComponent } from './catalogo.component';

describe('Catalogo Management Component', () => {
  let comp: CatalogoComponent;
  let fixture: ComponentFixture<CatalogoComponent>;
  let service: CatalogoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'catalogo', component: CatalogoComponent }]),
        HttpClientTestingModule,
        CatalogoComponent,
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
      .overrideTemplate(CatalogoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CatalogoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CatalogoService);

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
    expect(comp.catalogos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to catalogoService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getCatalogoIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getCatalogoIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
