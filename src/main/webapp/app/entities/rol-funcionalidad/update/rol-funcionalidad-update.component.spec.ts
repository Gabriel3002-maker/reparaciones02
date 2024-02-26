import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';
import { FuncionalidadService } from 'app/entities/funcionalidad/service/funcionalidad.service';
import { RolFuncionalidadService } from '../service/rol-funcionalidad.service';
import { IRolFuncionalidad } from '../rol-funcionalidad.model';
import { RolFuncionalidadFormService } from './rol-funcionalidad-form.service';

import { RolFuncionalidadUpdateComponent } from './rol-funcionalidad-update.component';

describe('RolFuncionalidad Management Update Component', () => {
  let comp: RolFuncionalidadUpdateComponent;
  let fixture: ComponentFixture<RolFuncionalidadUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rolFuncionalidadFormService: RolFuncionalidadFormService;
  let rolFuncionalidadService: RolFuncionalidadService;
  let funcionalidadService: FuncionalidadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), RolFuncionalidadUpdateComponent],
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
      .overrideTemplate(RolFuncionalidadUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RolFuncionalidadUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rolFuncionalidadFormService = TestBed.inject(RolFuncionalidadFormService);
    rolFuncionalidadService = TestBed.inject(RolFuncionalidadService);
    funcionalidadService = TestBed.inject(FuncionalidadService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Funcionalidad query and add missing value', () => {
      const rolFuncionalidad: IRolFuncionalidad = { id: 456 };
      const funcionalidad: IFuncionalidad = { id: 19367 };
      rolFuncionalidad.funcionalidad = funcionalidad;

      const funcionalidadCollection: IFuncionalidad[] = [{ id: 13383 }];
      jest.spyOn(funcionalidadService, 'query').mockReturnValue(of(new HttpResponse({ body: funcionalidadCollection })));
      const additionalFuncionalidads = [funcionalidad];
      const expectedCollection: IFuncionalidad[] = [...additionalFuncionalidads, ...funcionalidadCollection];
      jest.spyOn(funcionalidadService, 'addFuncionalidadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rolFuncionalidad });
      comp.ngOnInit();

      expect(funcionalidadService.query).toHaveBeenCalled();
      expect(funcionalidadService.addFuncionalidadToCollectionIfMissing).toHaveBeenCalledWith(
        funcionalidadCollection,
        ...additionalFuncionalidads.map(expect.objectContaining),
      );
      expect(comp.funcionalidadsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const rolFuncionalidad: IRolFuncionalidad = { id: 456 };
      const funcionalidad: IFuncionalidad = { id: 13887 };
      rolFuncionalidad.funcionalidad = funcionalidad;

      activatedRoute.data = of({ rolFuncionalidad });
      comp.ngOnInit();

      expect(comp.funcionalidadsSharedCollection).toContain(funcionalidad);
      expect(comp.rolFuncionalidad).toEqual(rolFuncionalidad);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRolFuncionalidad>>();
      const rolFuncionalidad = { id: 123 };
      jest.spyOn(rolFuncionalidadFormService, 'getRolFuncionalidad').mockReturnValue(rolFuncionalidad);
      jest.spyOn(rolFuncionalidadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rolFuncionalidad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rolFuncionalidad }));
      saveSubject.complete();

      // THEN
      expect(rolFuncionalidadFormService.getRolFuncionalidad).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(rolFuncionalidadService.update).toHaveBeenCalledWith(expect.objectContaining(rolFuncionalidad));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRolFuncionalidad>>();
      const rolFuncionalidad = { id: 123 };
      jest.spyOn(rolFuncionalidadFormService, 'getRolFuncionalidad').mockReturnValue({ id: null });
      jest.spyOn(rolFuncionalidadService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rolFuncionalidad: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rolFuncionalidad }));
      saveSubject.complete();

      // THEN
      expect(rolFuncionalidadFormService.getRolFuncionalidad).toHaveBeenCalled();
      expect(rolFuncionalidadService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRolFuncionalidad>>();
      const rolFuncionalidad = { id: 123 };
      jest.spyOn(rolFuncionalidadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rolFuncionalidad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rolFuncionalidadService.update).toHaveBeenCalled();
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
