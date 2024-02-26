import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../catalogo-item.test-samples';

import { CatalogoItemFormService } from './catalogo-item-form.service';

describe('CatalogoItem Form Service', () => {
  let service: CatalogoItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CatalogoItemFormService);
  });

  describe('Service methods', () => {
    describe('createCatalogoItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCatalogoItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            codigo: expect.any(Object),
            descripcion: expect.any(Object),
            catalogoCodigo: expect.any(Object),
            activo: expect.any(Object),
            catalogo: expect.any(Object),
          }),
        );
      });

      it('passing ICatalogoItem should create a new form with FormGroup', () => {
        const formGroup = service.createCatalogoItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            codigo: expect.any(Object),
            descripcion: expect.any(Object),
            catalogoCodigo: expect.any(Object),
            activo: expect.any(Object),
            catalogo: expect.any(Object),
          }),
        );
      });
    });

    describe('getCatalogoItem', () => {
      it('should return NewCatalogoItem for default CatalogoItem initial value', () => {
        const formGroup = service.createCatalogoItemFormGroup(sampleWithNewData);

        const catalogoItem = service.getCatalogoItem(formGroup) as any;

        expect(catalogoItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewCatalogoItem for empty CatalogoItem initial value', () => {
        const formGroup = service.createCatalogoItemFormGroup();

        const catalogoItem = service.getCatalogoItem(formGroup) as any;

        expect(catalogoItem).toMatchObject({});
      });

      it('should return ICatalogoItem', () => {
        const formGroup = service.createCatalogoItemFormGroup(sampleWithRequiredData);

        const catalogoItem = service.getCatalogoItem(formGroup) as any;

        expect(catalogoItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICatalogoItem should not enable id FormControl', () => {
        const formGroup = service.createCatalogoItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCatalogoItem should disable id FormControl', () => {
        const formGroup = service.createCatalogoItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
