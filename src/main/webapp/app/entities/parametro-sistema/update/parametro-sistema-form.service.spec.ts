import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../parametro-sistema.test-samples';

import { ParametroSistemaFormService } from './parametro-sistema-form.service';

describe('ParametroSistema Form Service', () => {
  let service: ParametroSistemaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ParametroSistemaFormService);
  });

  describe('Service methods', () => {
    describe('createParametroSistemaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createParametroSistemaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            codigo: expect.any(Object),
            clase: expect.any(Object),
            valor: expect.any(Object),
          }),
        );
      });

      it('passing IParametroSistema should create a new form with FormGroup', () => {
        const formGroup = service.createParametroSistemaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            codigo: expect.any(Object),
            clase: expect.any(Object),
            valor: expect.any(Object),
          }),
        );
      });
    });

    describe('getParametroSistema', () => {
      it('should return NewParametroSistema for default ParametroSistema initial value', () => {
        const formGroup = service.createParametroSistemaFormGroup(sampleWithNewData);

        const parametroSistema = service.getParametroSistema(formGroup) as any;

        expect(parametroSistema).toMatchObject(sampleWithNewData);
      });

      it('should return NewParametroSistema for empty ParametroSistema initial value', () => {
        const formGroup = service.createParametroSistemaFormGroup();

        const parametroSistema = service.getParametroSistema(formGroup) as any;

        expect(parametroSistema).toMatchObject({});
      });

      it('should return IParametroSistema', () => {
        const formGroup = service.createParametroSistemaFormGroup(sampleWithRequiredData);

        const parametroSistema = service.getParametroSistema(formGroup) as any;

        expect(parametroSistema).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IParametroSistema should not enable id FormControl', () => {
        const formGroup = service.createParametroSistemaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewParametroSistema should disable id FormControl', () => {
        const formGroup = service.createParametroSistemaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
