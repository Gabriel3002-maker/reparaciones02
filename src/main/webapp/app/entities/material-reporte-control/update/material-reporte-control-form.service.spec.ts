import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../material-reporte-control.test-samples';

import { MaterialReporteControlFormService } from './material-reporte-control-form.service';

describe('MaterialReporteControl Form Service', () => {
  let service: MaterialReporteControlFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaterialReporteControlFormService);
  });

  describe('Service methods', () => {
    describe('createMaterialReporteControlFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMaterialReporteControlFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            cantidadUsada: expect.any(Object),
            observacion: expect.any(Object),
          }),
        );
      });

      it('passing IMaterialReporteControl should create a new form with FormGroup', () => {
        const formGroup = service.createMaterialReporteControlFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            cantidadUsada: expect.any(Object),
            observacion: expect.any(Object),
          }),
        );
      });
    });

    describe('getMaterialReporteControl', () => {
      it('should return NewMaterialReporteControl for default MaterialReporteControl initial value', () => {
        const formGroup = service.createMaterialReporteControlFormGroup(sampleWithNewData);

        const materialReporteControl = service.getMaterialReporteControl(formGroup) as any;

        expect(materialReporteControl).toMatchObject(sampleWithNewData);
      });

      it('should return NewMaterialReporteControl for empty MaterialReporteControl initial value', () => {
        const formGroup = service.createMaterialReporteControlFormGroup();

        const materialReporteControl = service.getMaterialReporteControl(formGroup) as any;

        expect(materialReporteControl).toMatchObject({});
      });

      it('should return IMaterialReporteControl', () => {
        const formGroup = service.createMaterialReporteControlFormGroup(sampleWithRequiredData);

        const materialReporteControl = service.getMaterialReporteControl(formGroup) as any;

        expect(materialReporteControl).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMaterialReporteControl should not enable id FormControl', () => {
        const formGroup = service.createMaterialReporteControlFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMaterialReporteControl should disable id FormControl', () => {
        const formGroup = service.createMaterialReporteControlFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
