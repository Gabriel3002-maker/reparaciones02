import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MaterialDanioService } from '../service/material-danio.service';

import { MaterialDanioComponent } from './material-danio.component';

describe('MaterialDanio Management Component', () => {
  let comp: MaterialDanioComponent;
  let fixture: ComponentFixture<MaterialDanioComponent>;
  let service: MaterialDanioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'material-danio', component: MaterialDanioComponent }]),
        HttpClientTestingModule,
        MaterialDanioComponent,
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
      .overrideTemplate(MaterialDanioComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaterialDanioComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MaterialDanioService);

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
    expect(comp.materialDanios?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to materialDanioService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getMaterialDanioIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getMaterialDanioIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
