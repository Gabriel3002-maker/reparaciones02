import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ParametroSistemaService } from '../service/parametro-sistema.service';
import { IParametroSistema } from '../parametro-sistema.model';
import { ParametroSistemaFormService } from './parametro-sistema-form.service';

import { ParametroSistemaUpdateComponent } from './parametro-sistema-update.component';

describe('ParametroSistema Management Update Component', () => {
  let comp: ParametroSistemaUpdateComponent;
  let fixture: ComponentFixture<ParametroSistemaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let parametroSistemaFormService: ParametroSistemaFormService;
  let parametroSistemaService: ParametroSistemaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ParametroSistemaUpdateComponent],
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
      .overrideTemplate(ParametroSistemaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ParametroSistemaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    parametroSistemaFormService = TestBed.inject(ParametroSistemaFormService);
    parametroSistemaService = TestBed.inject(ParametroSistemaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const parametroSistema: IParametroSistema = { id: 456 };

      activatedRoute.data = of({ parametroSistema });
      comp.ngOnInit();

      expect(comp.parametroSistema).toEqual(parametroSistema);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParametroSistema>>();
      const parametroSistema = { id: 123 };
      jest.spyOn(parametroSistemaFormService, 'getParametroSistema').mockReturnValue(parametroSistema);
      jest.spyOn(parametroSistemaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parametroSistema });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: parametroSistema }));
      saveSubject.complete();

      // THEN
      expect(parametroSistemaFormService.getParametroSistema).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(parametroSistemaService.update).toHaveBeenCalledWith(expect.objectContaining(parametroSistema));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParametroSistema>>();
      const parametroSistema = { id: 123 };
      jest.spyOn(parametroSistemaFormService, 'getParametroSistema').mockReturnValue({ id: null });
      jest.spyOn(parametroSistemaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parametroSistema: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: parametroSistema }));
      saveSubject.complete();

      // THEN
      expect(parametroSistemaFormService.getParametroSistema).toHaveBeenCalled();
      expect(parametroSistemaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParametroSistema>>();
      const parametroSistema = { id: 123 };
      jest.spyOn(parametroSistemaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parametroSistema });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(parametroSistemaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
