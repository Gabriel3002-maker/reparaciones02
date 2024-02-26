import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CatalogoItemService } from '../service/catalogo-item.service';

import { CatalogoItemComponent } from './catalogo-item.component';

describe('CatalogoItem Management Component', () => {
  let comp: CatalogoItemComponent;
  let fixture: ComponentFixture<CatalogoItemComponent>;
  let service: CatalogoItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'catalogo-item', component: CatalogoItemComponent }]),
        HttpClientTestingModule,
        CatalogoItemComponent,
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
      .overrideTemplate(CatalogoItemComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CatalogoItemComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CatalogoItemService);

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
    expect(comp.catalogoItems?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to catalogoItemService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getCatalogoItemIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getCatalogoItemIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
