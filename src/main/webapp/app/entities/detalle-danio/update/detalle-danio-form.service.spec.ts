import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../detalle-danio.test-samples';

import { DetalleDanioFormService } from './detalle-danio-form.service';

describe('DetalleDanio Form Service', () => {
  let service: DetalleDanioFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DetalleDanioFormService);
  });

  describe('Service methods', () => {
    describe('createDetalleDanioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDetalleDanioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            descripcionDanio: expect.any(Object),
            tecnico: expect.any(Object),
            namePerson: expect.any(Object),
            direccion: expect.any(Object),
            estadoReparacion: expect.any(Object),
            observacion: expect.any(Object),
            tipoDanio: expect.any(Object),
            materialDanio: expect.any(Object),
            maquinaria: expect.any(Object),
            ordenBodega: expect.any(Object),
            detalleReporteDanio: expect.any(Object),
          }),
        );
      });

      it('passing IDetalleDanio should create a new form with FormGroup', () => {
        const formGroup = service.createDetalleDanioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            descripcionDanio: expect.any(Object),
            tecnico: expect.any(Object),
            namePerson: expect.any(Object),
            direccion: expect.any(Object),
            estadoReparacion: expect.any(Object),
            observacion: expect.any(Object),
            tipoDanio: expect.any(Object),
            materialDanio: expect.any(Object),
            maquinaria: expect.any(Object),
            ordenBodega: expect.any(Object),
            detalleReporteDanio: expect.any(Object),
          }),
        );
      });
    });

    describe('getDetalleDanio', () => {
      it('should return NewDetalleDanio for default DetalleDanio initial value', () => {
        const formGroup = service.createDetalleDanioFormGroup(sampleWithNewData);

        const detalleDanio = service.getDetalleDanio(formGroup) as any;

        expect(detalleDanio).toMatchObject(sampleWithNewData);
      });

      it('should return NewDetalleDanio for empty DetalleDanio initial value', () => {
        const formGroup = service.createDetalleDanioFormGroup();

        const detalleDanio = service.getDetalleDanio(formGroup) as any;

        expect(detalleDanio).toMatchObject({});
      });

      it('should return IDetalleDanio', () => {
        const formGroup = service.createDetalleDanioFormGroup(sampleWithRequiredData);

        const detalleDanio = service.getDetalleDanio(formGroup) as any;

        expect(detalleDanio).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDetalleDanio should not enable id FormControl', () => {
        const formGroup = service.createDetalleDanioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDetalleDanio should disable id FormControl', () => {
        const formGroup = service.createDetalleDanioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
