import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RegistroDanioService } from '../service/registro-danio.service';

import { RegistroDanioComponent } from './registro-danio.component';

describe('RegistroDanio Management Component', () => {
  let comp: RegistroDanioComponent;
  let fixture: ComponentFixture<RegistroDanioComponent>;
  let service: RegistroDanioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'registro-danio', component: RegistroDanioComponent }]),
        HttpClientTestingModule,
        RegistroDanioComponent,
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
      .overrideTemplate(RegistroDanioComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RegistroDanioComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RegistroDanioService);

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
    expect(comp.registroDanios?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to registroDanioService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getRegistroDanioIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getRegistroDanioIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
