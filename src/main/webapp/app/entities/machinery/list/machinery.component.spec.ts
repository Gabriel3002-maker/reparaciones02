import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MachineryService } from '../service/machinery.service';

import { MachineryComponent } from './machinery.component';

describe('Machinery Management Component', () => {
  let comp: MachineryComponent;
  let fixture: ComponentFixture<MachineryComponent>;
  let service: MachineryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'machinery', component: MachineryComponent }]),
        HttpClientTestingModule,
        MachineryComponent,
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
      .overrideTemplate(MachineryComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MachineryComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MachineryService);

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
    expect(comp.machinery?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to machineryService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getMachineryIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getMachineryIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
