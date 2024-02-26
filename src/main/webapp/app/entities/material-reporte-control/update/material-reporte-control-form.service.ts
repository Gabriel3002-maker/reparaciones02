import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMaterialReporteControl, NewMaterialReporteControl } from '../material-reporte-control.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMaterialReporteControl for edit and NewMaterialReporteControlFormGroupInput for create.
 */
type MaterialReporteControlFormGroupInput = IMaterialReporteControl | PartialWithRequiredKeyOf<NewMaterialReporteControl>;

type MaterialReporteControlFormDefaults = Pick<NewMaterialReporteControl, 'id'>;

type MaterialReporteControlFormGroupContent = {
  id: FormControl<IMaterialReporteControl['id'] | NewMaterialReporteControl['id']>;
  codigo: FormControl<IMaterialReporteControl['codigo']>;
  cantidadUsada: FormControl<IMaterialReporteControl['cantidadUsada']>;
  observacion: FormControl<IMaterialReporteControl['observacion']>;
};

export type MaterialReporteControlFormGroup = FormGroup<MaterialReporteControlFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MaterialReporteControlFormService {
  createMaterialReporteControlFormGroup(
    materialReporteControl: MaterialReporteControlFormGroupInput = { id: null },
  ): MaterialReporteControlFormGroup {
    const materialReporteControlRawValue = {
      ...this.getFormDefaults(),
      ...materialReporteControl,
    };
    return new FormGroup<MaterialReporteControlFormGroupContent>({
      id: new FormControl(
        { value: materialReporteControlRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      codigo: new FormControl(materialReporteControlRawValue.codigo),
      cantidadUsada: new FormControl(materialReporteControlRawValue.cantidadUsada),
      observacion: new FormControl(materialReporteControlRawValue.observacion),
    });
  }

  getMaterialReporteControl(form: MaterialReporteControlFormGroup): IMaterialReporteControl | NewMaterialReporteControl {
    return form.getRawValue() as IMaterialReporteControl | NewMaterialReporteControl;
  }

  resetForm(form: MaterialReporteControlFormGroup, materialReporteControl: MaterialReporteControlFormGroupInput): void {
    const materialReporteControlRawValue = { ...this.getFormDefaults(), ...materialReporteControl };
    form.reset(
      {
        ...materialReporteControlRawValue,
        id: { value: materialReporteControlRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MaterialReporteControlFormDefaults {
    return {
      id: null,
    };
  }
}
