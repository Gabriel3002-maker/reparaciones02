import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IPersona } from 'app/entities/persona/persona.model';
import { PersonaService } from 'app/entities/persona/service/persona.service';
import { OrdenBodegaService } from '../service/orden-bodega.service';
import { IOrdenBodega } from '../orden-bodega.model';
import { OrdenBodegaFormService } from './orden-bodega-form.service';

import { OrdenBodegaUpdateComponent } from './orden-bodega-update.component';

describe('OrdenBodega Management Update Component', () => {
  let comp: OrdenBodegaUpdateComponent;
  let fixture: ComponentFixture<OrdenBodegaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ordenBodegaFormService: OrdenBodegaFormService;
  let ordenBodegaService: OrdenBodegaService;
  let personaService: PersonaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OrdenBodegaUpdateComponent],
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
      .overrideTemplate(OrdenBodegaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrdenBodegaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ordenBodegaFormService = TestBed.inject(OrdenBodegaFormService);
    ordenBodegaService = TestBed.inject(OrdenBodegaService);
    personaService = TestBed.inject(PersonaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Persona query and add missing value', () => {
      const ordenBodega: IOrdenBodega = { id: 456 };
      const receptor: IPersona = { id: 28185 };
      ordenBodega.receptor = receptor;

      const personaCollection: IPersona[] = [{ id: 12279 }];
      jest.spyOn(personaService, 'query').mockReturnValue(of(new HttpResponse({ body: personaCollection })));
      const additionalPersonas = [receptor];
      const expectedCollection: IPersona[] = [...additionalPersonas, ...personaCollection];
      jest.spyOn(personaService, 'addPersonaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ordenBodega });
      comp.ngOnInit();

      expect(personaService.query).toHaveBeenCalled();
      expect(personaService.addPersonaToCollectionIfMissing).toHaveBeenCalledWith(
        personaCollection,
        ...additionalPersonas.map(expect.objectContaining),
      );
      expect(comp.personasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ordenBodega: IOrdenBodega = { id: 456 };
      const receptor: IPersona = { id: 2230 };
      ordenBodega.receptor = receptor;

      activatedRoute.data = of({ ordenBodega });
      comp.ngOnInit();

      expect(comp.personasSharedCollection).toContain(receptor);
      expect(comp.ordenBodega).toEqual(ordenBodega);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrdenBodega>>();
      const ordenBodega = { id: 123 };
      jest.spyOn(ordenBodegaFormService, 'getOrdenBodega').mockReturnValue(ordenBodega);
      jest.spyOn(ordenBodegaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ordenBodega });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ordenBodega }));
      saveSubject.complete();

      // THEN
      expect(ordenBodegaFormService.getOrdenBodega).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ordenBodegaService.update).toHaveBeenCalledWith(expect.objectContaining(ordenBodega));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrdenBodega>>();
      const ordenBodega = { id: 123 };
      jest.spyOn(ordenBodegaFormService, 'getOrdenBodega').mockReturnValue({ id: null });
      jest.spyOn(ordenBodegaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ordenBodega: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ordenBodega }));
      saveSubject.complete();

      // THEN
      expect(ordenBodegaFormService.getOrdenBodega).toHaveBeenCalled();
      expect(ordenBodegaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrdenBodega>>();
      const ordenBodega = { id: 123 };
      jest.spyOn(ordenBodegaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ordenBodega });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ordenBodegaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePersona', () => {
      it('Should forward to personaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(personaService, 'comparePersona');
        comp.comparePersona(entity, entity2);
        expect(personaService.comparePersona).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
