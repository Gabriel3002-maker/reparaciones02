import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../funcionalidad.test-samples';

import { FuncionalidadFormService } from './funcionalidad-form.service';

describe('Funcionalidad Form Service', () => {
  let service: FuncionalidadFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FuncionalidadFormService);
  });

  describe('Service methods', () => {
    describe('createFuncionalidadFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFuncionalidadFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            url: expect.any(Object),
            activo: expect.any(Object),
            icono: expect.any(Object),
            visible: expect.any(Object),
            padre: expect.any(Object),
          }),
        );
      });

      it('passing IFuncionalidad should create a new form with FormGroup', () => {
        const formGroup = service.createFuncionalidadFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            url: expect.any(Object),
            activo: expect.any(Object),
            icono: expect.any(Object),
            visible: expect.any(Object),
            padre: expect.any(Object),
          }),
        );
      });
    });

    describe('getFuncionalidad', () => {
      it('should return NewFuncionalidad for default Funcionalidad initial value', () => {
        const formGroup = service.createFuncionalidadFormGroup(sampleWithNewData);

        const funcionalidad = service.getFuncionalidad(formGroup) as any;

        expect(funcionalidad).toMatchObject(sampleWithNewData);
      });

      it('should return NewFuncionalidad for empty Funcionalidad initial value', () => {
        const formGroup = service.createFuncionalidadFormGroup();

        const funcionalidad = service.getFuncionalidad(formGroup) as any;

        expect(funcionalidad).toMatchObject({});
      });

      it('should return IFuncionalidad', () => {
        const formGroup = service.createFuncionalidadFormGroup(sampleWithRequiredData);

        const funcionalidad = service.getFuncionalidad(formGroup) as any;

        expect(funcionalidad).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFuncionalidad should not enable id FormControl', () => {
        const formGroup = service.createFuncionalidadFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFuncionalidad should disable id FormControl', () => {
        const formGroup = service.createFuncionalidadFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
