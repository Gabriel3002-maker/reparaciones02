import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../detalle-reporte-danio.test-samples';

import { DetalleReporteDanioFormService } from './detalle-reporte-danio-form.service';

describe('DetalleReporteDanio Form Service', () => {
  let service: DetalleReporteDanioFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DetalleReporteDanioFormService);
  });

  describe('Service methods', () => {
    describe('createDetalleReporteDanioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDetalleReporteDanioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            fecha: expect.any(Object),
            contribuyente: expect.any(Object),
            direccion: expect.any(Object),
            referencia: expect.any(Object),
            horasTrabajadas: expect.any(Object),
            personalResponsable: expect.any(Object),
            materialReporte: expect.any(Object),
          }),
        );
      });

      it('passing IDetalleReporteDanio should create a new form with FormGroup', () => {
        const formGroup = service.createDetalleReporteDanioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            fecha: expect.any(Object),
            contribuyente: expect.any(Object),
            direccion: expect.any(Object),
            referencia: expect.any(Object),
            horasTrabajadas: expect.any(Object),
            personalResponsable: expect.any(Object),
            materialReporte: expect.any(Object),
          }),
        );
      });
    });

    describe('getDetalleReporteDanio', () => {
      it('should return NewDetalleReporteDanio for default DetalleReporteDanio initial value', () => {
        const formGroup = service.createDetalleReporteDanioFormGroup(sampleWithNewData);

        const detalleReporteDanio = service.getDetalleReporteDanio(formGroup) as any;

        expect(detalleReporteDanio).toMatchObject(sampleWithNewData);
      });

      it('should return NewDetalleReporteDanio for empty DetalleReporteDanio initial value', () => {
        const formGroup = service.createDetalleReporteDanioFormGroup();

        const detalleReporteDanio = service.getDetalleReporteDanio(formGroup) as any;

        expect(detalleReporteDanio).toMatchObject({});
      });

      it('should return IDetalleReporteDanio', () => {
        const formGroup = service.createDetalleReporteDanioFormGroup(sampleWithRequiredData);

        const detalleReporteDanio = service.getDetalleReporteDanio(formGroup) as any;

        expect(detalleReporteDanio).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDetalleReporteDanio should not enable id FormControl', () => {
        const formGroup = service.createDetalleReporteDanioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDetalleReporteDanio should disable id FormControl', () => {
        const formGroup = service.createDetalleReporteDanioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
