import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IMaterialReporteControl } from 'app/entities/material-reporte-control/material-reporte-control.model';
import { MaterialReporteControlService } from 'app/entities/material-reporte-control/service/material-reporte-control.service';
import { DetalleReporteDanioService } from '../service/detalle-reporte-danio.service';
import { IDetalleReporteDanio } from '../detalle-reporte-danio.model';
import { DetalleReporteDanioFormService } from './detalle-reporte-danio-form.service';

import { DetalleReporteDanioUpdateComponent } from './detalle-reporte-danio-update.component';

describe('DetalleReporteDanio Management Update Component', () => {
  let comp: DetalleReporteDanioUpdateComponent;
  let fixture: ComponentFixture<DetalleReporteDanioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let detalleReporteDanioFormService: DetalleReporteDanioFormService;
  let detalleReporteDanioService: DetalleReporteDanioService;
  let materialReporteControlService: MaterialReporteControlService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DetalleReporteDanioUpdateComponent],
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
      .overrideTemplate(DetalleReporteDanioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DetalleReporteDanioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    detalleReporteDanioFormService = TestBed.inject(DetalleReporteDanioFormService);
    detalleReporteDanioService = TestBed.inject(DetalleReporteDanioService);
    materialReporteControlService = TestBed.inject(MaterialReporteControlService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MaterialReporteControl query and add missing value', () => {
      const detalleReporteDanio: IDetalleReporteDanio = { id: 456 };
      const materialReporte: IMaterialReporteControl = { id: 17451 };
      detalleReporteDanio.materialReporte = materialReporte;

      const materialReporteControlCollection: IMaterialReporteControl[] = [{ id: 3184 }];
      jest.spyOn(materialReporteControlService, 'query').mockReturnValue(of(new HttpResponse({ body: materialReporteControlCollection })));
      const additionalMaterialReporteControls = [materialReporte];
      const expectedCollection: IMaterialReporteControl[] = [...additionalMaterialReporteControls, ...materialReporteControlCollection];
      jest.spyOn(materialReporteControlService, 'addMaterialReporteControlToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detalleReporteDanio });
      comp.ngOnInit();

      expect(materialReporteControlService.query).toHaveBeenCalled();
      expect(materialReporteControlService.addMaterialReporteControlToCollectionIfMissing).toHaveBeenCalledWith(
        materialReporteControlCollection,
        ...additionalMaterialReporteControls.map(expect.objectContaining),
      );
      expect(comp.materialReporteControlsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const detalleReporteDanio: IDetalleReporteDanio = { id: 456 };
      const materialReporte: IMaterialReporteControl = { id: 32548 };
      detalleReporteDanio.materialReporte = materialReporte;

      activatedRoute.data = of({ detalleReporteDanio });
      comp.ngOnInit();

      expect(comp.materialReporteControlsSharedCollection).toContain(materialReporte);
      expect(comp.detalleReporteDanio).toEqual(detalleReporteDanio);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDetalleReporteDanio>>();
      const detalleReporteDanio = { id: 123 };
      jest.spyOn(detalleReporteDanioFormService, 'getDetalleReporteDanio').mockReturnValue(detalleReporteDanio);
      jest.spyOn(detalleReporteDanioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleReporteDanio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detalleReporteDanio }));
      saveSubject.complete();

      // THEN
      expect(detalleReporteDanioFormService.getDetalleReporteDanio).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(detalleReporteDanioService.update).toHaveBeenCalledWith(expect.objectContaining(detalleReporteDanio));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDetalleReporteDanio>>();
      const detalleReporteDanio = { id: 123 };
      jest.spyOn(detalleReporteDanioFormService, 'getDetalleReporteDanio').mockReturnValue({ id: null });
      jest.spyOn(detalleReporteDanioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleReporteDanio: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detalleReporteDanio }));
      saveSubject.complete();

      // THEN
      expect(detalleReporteDanioFormService.getDetalleReporteDanio).toHaveBeenCalled();
      expect(detalleReporteDanioService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDetalleReporteDanio>>();
      const detalleReporteDanio = { id: 123 };
      jest.spyOn(detalleReporteDanioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleReporteDanio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(detalleReporteDanioService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMaterialReporteControl', () => {
      it('Should forward to materialReporteControlService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(materialReporteControlService, 'compareMaterialReporteControl');
        comp.compareMaterialReporteControl(entity, entity2);
        expect(materialReporteControlService.compareMaterialReporteControl).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
