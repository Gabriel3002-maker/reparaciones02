import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../material-danio.test-samples';

import { MaterialDanioFormService } from './material-danio-form.service';

describe('MaterialDanio Form Service', () => {
  let service: MaterialDanioFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaterialDanioFormService);
  });

  describe('Service methods', () => {
    describe('createMaterialDanioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMaterialDanioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            cantidadPedida: expect.any(Object),
            observacion: expect.any(Object),
          }),
        );
      });

      it('passing IMaterialDanio should create a new form with FormGroup', () => {
        const formGroup = service.createMaterialDanioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            cantidadPedida: expect.any(Object),
            observacion: expect.any(Object),
          }),
        );
      });
    });

    describe('getMaterialDanio', () => {
      it('should return NewMaterialDanio for default MaterialDanio initial value', () => {
        const formGroup = service.createMaterialDanioFormGroup(sampleWithNewData);

        const materialDanio = service.getMaterialDanio(formGroup) as any;

        expect(materialDanio).toMatchObject(sampleWithNewData);
      });

      it('should return NewMaterialDanio for empty MaterialDanio initial value', () => {
        const formGroup = service.createMaterialDanioFormGroup();

        const materialDanio = service.getMaterialDanio(formGroup) as any;

        expect(materialDanio).toMatchObject({});
      });

      it('should return IMaterialDanio', () => {
        const formGroup = service.createMaterialDanioFormGroup(sampleWithRequiredData);

        const materialDanio = service.getMaterialDanio(formGroup) as any;

        expect(materialDanio).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMaterialDanio should not enable id FormControl', () => {
        const formGroup = service.createMaterialDanioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMaterialDanio should disable id FormControl', () => {
        const formGroup = service.createMaterialDanioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
