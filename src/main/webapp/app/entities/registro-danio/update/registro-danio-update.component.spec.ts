import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IDetalleDanio } from 'app/entities/detalle-danio/detalle-danio.model';
import { DetalleDanioService } from 'app/entities/detalle-danio/service/detalle-danio.service';
import { RegistroDanioService } from '../service/registro-danio.service';
import { IRegistroDanio } from '../registro-danio.model';
import { RegistroDanioFormService } from './registro-danio-form.service';

import { RegistroDanioUpdateComponent } from './registro-danio-update.component';

describe('RegistroDanio Management Update Component', () => {
  let comp: RegistroDanioUpdateComponent;
  let fixture: ComponentFixture<RegistroDanioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let registroDanioFormService: RegistroDanioFormService;
  let registroDanioService: RegistroDanioService;
  let detalleDanioService: DetalleDanioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), RegistroDanioUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(RegistroDanioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RegistroDanioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    registroDanioFormService = TestBed.inject(RegistroDanioFormService);
    registroDanioService = TestBed.inject(RegistroDanioService);
    detalleDanioService = TestBed.inject(DetalleDanioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call DetalleDanio query and add missing value', () => {
      const registroDanio: IRegistroDanio = { id: 456 };
      const detalleDanio: IDetalleDanio = { id: 8446 };
      registroDanio.detalleDanio = detalleDanio;

      const detalleDanioCollection: IDetalleDanio[] = [{ id: 5486 }];
      jest.spyOn(detalleDanioService, 'query').mockReturnValue(of(new HttpResponse({ body: detalleDanioCollection })));
      const additionalDetalleDanios = [detalleDanio];
      const expectedCollection: IDetalleDanio[] = [...additionalDetalleDanios, ...detalleDanioCollection];
      jest.spyOn(detalleDanioService, 'addDetalleDanioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ registroDanio });
      comp.ngOnInit();

      expect(detalleDanioService.query).toHaveBeenCalled();
      expect(detalleDanioService.addDetalleDanioToCollectionIfMissing).toHaveBeenCalledWith(
        detalleDanioCollection,
        ...additionalDetalleDanios.map(expect.objectContaining),
      );
      expect(comp.detalleDaniosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const registroDanio: IRegistroDanio = { id: 456 };
      const detalleDanio: IDetalleDanio = { id: 4579 };
      registroDanio.detalleDanio = detalleDanio;

      activatedRoute.data = of({ registroDanio });
      comp.ngOnInit();

      expect(comp.detalleDaniosSharedCollection).toContain(detalleDanio);
      expect(comp.registroDanio).toEqual(registroDanio);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRegistroDanio>>();
      const registroDanio = { id: 123 };
      jest.spyOn(registroDanioFormService, 'getRegistroDanio').mockReturnValue(registroDanio);
      jest.spyOn(registroDanioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ registroDanio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: registroDanio }));
      saveSubject.complete();

      // THEN
      expect(registroDanioFormService.getRegistroDanio).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(registroDanioService.update).toHaveBeenCalledWith(expect.objectContaining(registroDanio));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRegistroDanio>>();
      const registroDanio = { id: 123 };
      jest.spyOn(registroDanioFormService, 'getRegistroDanio').mockReturnValue({ id: null });
      jest.spyOn(registroDanioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ registroDanio: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: registroDanio }));
      saveSubject.complete();

      // THEN
      expect(registroDanioFormService.getRegistroDanio).toHaveBeenCalled();
      expect(registroDanioService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRegistroDanio>>();
      const registroDanio = { id: 123 };
      jest.spyOn(registroDanioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ registroDanio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(registroDanioService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDetalleDanio', () => {
      it('Should forward to detalleDanioService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(detalleDanioService, 'compareDetalleDanio');
        comp.compareDetalleDanio(entity, entity2);
        expect(detalleDanioService.compareDetalleDanio).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
