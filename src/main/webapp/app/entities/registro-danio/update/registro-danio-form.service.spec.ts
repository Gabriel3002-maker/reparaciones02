import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../registro-danio.test-samples';

import { RegistroDanioFormService } from './registro-danio-form.service';

describe('RegistroDanio Form Service', () => {
  let service: RegistroDanioFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RegistroDanioFormService);
  });

  describe('Service methods', () => {
    describe('createRegistroDanioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRegistroDanioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            fecha: expect.any(Object),
            fechaInicio: expect.any(Object),
            fechaFin: expect.any(Object),
            direccion: expect.any(Object),
            parroquia: expect.any(Object),
            barrio: expect.any(Object),
            detalleDanio: expect.any(Object),
          }),
        );
      });

      it('passing IRegistroDanio should create a new form with FormGroup', () => {
        const formGroup = service.createRegistroDanioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            fecha: expect.any(Object),
            fechaInicio: expect.any(Object),
            fechaFin: expect.any(Object),
            direccion: expect.any(Object),
            parroquia: expect.any(Object),
            barrio: expect.any(Object),
            detalleDanio: expect.any(Object),
          }),
        );
      });
    });

    describe('getRegistroDanio', () => {
      it('should return NewRegistroDanio for default RegistroDanio initial value', () => {
        const formGroup = service.createRegistroDanioFormGroup(sampleWithNewData);

        const registroDanio = service.getRegistroDanio(formGroup) as any;

        expect(registroDanio).toMatchObject(sampleWithNewData);
      });

      it('should return NewRegistroDanio for empty RegistroDanio initial value', () => {
        const formGroup = service.createRegistroDanioFormGroup();

        const registroDanio = service.getRegistroDanio(formGroup) as any;

        expect(registroDanio).toMatchObject({});
      });

      it('should return IRegistroDanio', () => {
        const formGroup = service.createRegistroDanioFormGroup(sampleWithRequiredData);

        const registroDanio = service.getRegistroDanio(formGroup) as any;

        expect(registroDanio).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRegistroDanio should not enable id FormControl', () => {
        const formGroup = service.createRegistroDanioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRegistroDanio should disable id FormControl', () => {
        const formGroup = service.createRegistroDanioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
