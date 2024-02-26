import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MaterialReporteControlService } from '../service/material-reporte-control.service';
import { IMaterialReporteControl } from '../material-reporte-control.model';
import { MaterialReporteControlFormService } from './material-reporte-control-form.service';

import { MaterialReporteControlUpdateComponent } from './material-reporte-control-update.component';

describe('MaterialReporteControl Management Update Component', () => {
  let comp: MaterialReporteControlUpdateComponent;
  let fixture: ComponentFixture<MaterialReporteControlUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let materialReporteControlFormService: MaterialReporteControlFormService;
  let materialReporteControlService: MaterialReporteControlService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MaterialReporteControlUpdateComponent],
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
      .overrideTemplate(MaterialReporteControlUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaterialReporteControlUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    materialReporteControlFormService = TestBed.inject(MaterialReporteControlFormService);
    materialReporteControlService = TestBed.inject(MaterialReporteControlService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const materialReporteControl: IMaterialReporteControl = { id: 456 };

      activatedRoute.data = of({ materialReporteControl });
      comp.ngOnInit();

      expect(comp.materialReporteControl).toEqual(materialReporteControl);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialReporteControl>>();
      const materialReporteControl = { id: 123 };
      jest.spyOn(materialReporteControlFormService, 'getMaterialReporteControl').mockReturnValue(materialReporteControl);
      jest.spyOn(materialReporteControlService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialReporteControl });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materialReporteControl }));
      saveSubject.complete();

      // THEN
      expect(materialReporteControlFormService.getMaterialReporteControl).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(materialReporteControlService.update).toHaveBeenCalledWith(expect.objectContaining(materialReporteControl));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialReporteControl>>();
      const materialReporteControl = { id: 123 };
      jest.spyOn(materialReporteControlFormService, 'getMaterialReporteControl').mockReturnValue({ id: null });
      jest.spyOn(materialReporteControlService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialReporteControl: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materialReporteControl }));
      saveSubject.complete();

      // THEN
      expect(materialReporteControlFormService.getMaterialReporteControl).toHaveBeenCalled();
      expect(materialReporteControlService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialReporteControl>>();
      const materialReporteControl = { id: 123 };
      jest.spyOn(materialReporteControlService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialReporteControl });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(materialReporteControlService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
