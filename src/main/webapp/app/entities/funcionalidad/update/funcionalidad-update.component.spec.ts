import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FuncionalidadService } from '../service/funcionalidad.service';
import { IFuncionalidad } from '../funcionalidad.model';
import { FuncionalidadFormService } from './funcionalidad-form.service';

import { FuncionalidadUpdateComponent } from './funcionalidad-update.component';

describe('Funcionalidad Management Update Component', () => {
  let comp: FuncionalidadUpdateComponent;
  let fixture: ComponentFixture<FuncionalidadUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let funcionalidadFormService: FuncionalidadFormService;
  let funcionalidadService: FuncionalidadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FuncionalidadUpdateComponent],
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
      .overrideTemplate(FuncionalidadUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FuncionalidadUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    funcionalidadFormService = TestBed.inject(FuncionalidadFormService);
    funcionalidadService = TestBed.inject(FuncionalidadService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Funcionalidad query and add missing value', () => {
      const funcionalidad: IFuncionalidad = { id: 456 };
      const padre: IFuncionalidad = { id: 13027 };
      funcionalidad.padre = padre;

      const funcionalidadCollection: IFuncionalidad[] = [{ id: 26285 }];
      jest.spyOn(funcionalidadService, 'query').mockReturnValue(of(new HttpResponse({ body: funcionalidadCollection })));
      const additionalFuncionalidads = [padre];
      const expectedCollection: IFuncionalidad[] = [...additionalFuncionalidads, ...funcionalidadCollection];
      jest.spyOn(funcionalidadService, 'addFuncionalidadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ funcionalidad });
      comp.ngOnInit();

      expect(funcionalidadService.query).toHaveBeenCalled();
      expect(funcionalidadService.addFuncionalidadToCollectionIfMissing).toHaveBeenCalledWith(
        funcionalidadCollection,
        ...additionalFuncionalidads.map(expect.objectContaining),
      );
      expect(comp.funcionalidadsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const funcionalidad: IFuncionalidad = { id: 456 };
      const padre: IFuncionalidad = { id: 20762 };
      funcionalidad.padre = padre;

      activatedRoute.data = of({ funcionalidad });
      comp.ngOnInit();

      expect(comp.funcionalidadsSharedCollection).toContain(padre);
      expect(comp.funcionalidad).toEqual(funcionalidad);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFuncionalidad>>();
      const funcionalidad = { id: 123 };
      jest.spyOn(funcionalidadFormService, 'getFuncionalidad').mockReturnValue(funcionalidad);
      jest.spyOn(funcionalidadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ funcionalidad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: funcionalidad }));
      saveSubject.complete();

      // THEN
      expect(funcionalidadFormService.getFuncionalidad).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(funcionalidadService.update).toHaveBeenCalledWith(expect.objectContaining(funcionalidad));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFuncionalidad>>();
      const funcionalidad = { id: 123 };
      jest.spyOn(funcionalidadFormService, 'getFuncionalidad').mockReturnValue({ id: null });
      jest.spyOn(funcionalidadService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ funcionalidad: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: funcionalidad }));
      saveSubject.complete();

      // THEN
      expect(funcionalidadFormService.getFuncionalidad).toHaveBeenCalled();
      expect(funcionalidadService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFuncionalidad>>();
      const funcionalidad = { id: 123 };
      jest.spyOn(funcionalidadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ funcionalidad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(funcionalidadService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFuncionalidad', () => {
      it('Should forward to funcionalidadService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(funcionalidadService, 'compareFuncionalidad');
        comp.compareFuncionalidad(entity, entity2);
        expect(funcionalidadService.compareFuncionalidad).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
