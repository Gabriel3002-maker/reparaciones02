import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../orden-bodega.test-samples';

import { OrdenBodegaFormService } from './orden-bodega-form.service';

describe('OrdenBodega Form Service', () => {
  let service: OrdenBodegaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrdenBodegaFormService);
  });

  describe('Service methods', () => {
    describe('createOrdenBodegaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrdenBodegaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            detalleNecesidad: expect.any(Object),
            fecha: expect.any(Object),
            receptor: expect.any(Object),
          }),
        );
      });

      it('passing IOrdenBodega should create a new form with FormGroup', () => {
        const formGroup = service.createOrdenBodegaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            detalleNecesidad: expect.any(Object),
            fecha: expect.any(Object),
            receptor: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrdenBodega', () => {
      it('should return NewOrdenBodega for default OrdenBodega initial value', () => {
        const formGroup = service.createOrdenBodegaFormGroup(sampleWithNewData);

        const ordenBodega = service.getOrdenBodega(formGroup) as any;

        expect(ordenBodega).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrdenBodega for empty OrdenBodega initial value', () => {
        const formGroup = service.createOrdenBodegaFormGroup();

        const ordenBodega = service.getOrdenBodega(formGroup) as any;

        expect(ordenBodega).toMatchObject({});
      });

      it('should return IOrdenBodega', () => {
        const formGroup = service.createOrdenBodegaFormGroup(sampleWithRequiredData);

        const ordenBodega = service.getOrdenBodega(formGroup) as any;

        expect(ordenBodega).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrdenBodega should not enable id FormControl', () => {
        const formGroup = service.createOrdenBodegaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrdenBodega should disable id FormControl', () => {
        const formGroup = service.createOrdenBodegaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
