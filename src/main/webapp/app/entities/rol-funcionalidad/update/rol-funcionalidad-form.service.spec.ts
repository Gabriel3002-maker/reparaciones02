import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../rol-funcionalidad.test-samples';

import { RolFuncionalidadFormService } from './rol-funcionalidad-form.service';

describe('RolFuncionalidad Form Service', () => {
  let service: RolFuncionalidadFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RolFuncionalidadFormService);
  });

  describe('Service methods', () => {
    describe('createRolFuncionalidadFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRolFuncionalidadFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rol: expect.any(Object),
            activo: expect.any(Object),
            prioridad: expect.any(Object),
            funcionalidad: expect.any(Object),
          }),
        );
      });

      it('passing IRolFuncionalidad should create a new form with FormGroup', () => {
        const formGroup = service.createRolFuncionalidadFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rol: expect.any(Object),
            activo: expect.any(Object),
            prioridad: expect.any(Object),
            funcionalidad: expect.any(Object),
          }),
        );
      });
    });

    describe('getRolFuncionalidad', () => {
      it('should return NewRolFuncionalidad for default RolFuncionalidad initial value', () => {
        const formGroup = service.createRolFuncionalidadFormGroup(sampleWithNewData);

        const rolFuncionalidad = service.getRolFuncionalidad(formGroup) as any;

        expect(rolFuncionalidad).toMatchObject(sampleWithNewData);
      });

      it('should return NewRolFuncionalidad for empty RolFuncionalidad initial value', () => {
        const formGroup = service.createRolFuncionalidadFormGroup();

        const rolFuncionalidad = service.getRolFuncionalidad(formGroup) as any;

        expect(rolFuncionalidad).toMatchObject({});
      });

      it('should return IRolFuncionalidad', () => {
        const formGroup = service.createRolFuncionalidadFormGroup(sampleWithRequiredData);

        const rolFuncionalidad = service.getRolFuncionalidad(formGroup) as any;

        expect(rolFuncionalidad).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRolFuncionalidad should not enable id FormControl', () => {
        const formGroup = service.createRolFuncionalidadFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRolFuncionalidad should disable id FormControl', () => {
        const formGroup = service.createRolFuncionalidadFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
