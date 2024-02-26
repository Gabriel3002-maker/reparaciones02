import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../machinery.test-samples';

import { MachineryFormService } from './machinery-form.service';

describe('Machinery Form Service', () => {
  let service: MachineryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MachineryFormService);
  });

  describe('Service methods', () => {
    describe('createMachineryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMachineryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            descripcion: expect.any(Object),
            horasTrabajadas: expect.any(Object),
            nombre: expect.any(Object),
          }),
        );
      });

      it('passing IMachinery should create a new form with FormGroup', () => {
        const formGroup = service.createMachineryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            descripcion: expect.any(Object),
            horasTrabajadas: expect.any(Object),
            nombre: expect.any(Object),
          }),
        );
      });
    });

    describe('getMachinery', () => {
      it('should return NewMachinery for default Machinery initial value', () => {
        const formGroup = service.createMachineryFormGroup(sampleWithNewData);

        const machinery = service.getMachinery(formGroup) as any;

        expect(machinery).toMatchObject(sampleWithNewData);
      });

      it('should return NewMachinery for empty Machinery initial value', () => {
        const formGroup = service.createMachineryFormGroup();

        const machinery = service.getMachinery(formGroup) as any;

        expect(machinery).toMatchObject({});
      });

      it('should return IMachinery', () => {
        const formGroup = service.createMachineryFormGroup(sampleWithRequiredData);

        const machinery = service.getMachinery(formGroup) as any;

        expect(machinery).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMachinery should not enable id FormControl', () => {
        const formGroup = service.createMachineryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMachinery should disable id FormControl', () => {
        const formGroup = service.createMachineryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
