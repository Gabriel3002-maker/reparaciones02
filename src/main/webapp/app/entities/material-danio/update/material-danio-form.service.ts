import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMaterialDanio, NewMaterialDanio } from '../material-danio.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMaterialDanio for edit and NewMaterialDanioFormGroupInput for create.
 */
type MaterialDanioFormGroupInput = IMaterialDanio | PartialWithRequiredKeyOf<NewMaterialDanio>;

type MaterialDanioFormDefaults = Pick<NewMaterialDanio, 'id'>;

type MaterialDanioFormGroupContent = {
  id: FormControl<IMaterialDanio['id'] | NewMaterialDanio['id']>;
  codigo: FormControl<IMaterialDanio['codigo']>;
  cantidadPedida: FormControl<IMaterialDanio['cantidadPedida']>;
  observacion: FormControl<IMaterialDanio['observacion']>;
};

export type MaterialDanioFormGroup = FormGroup<MaterialDanioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MaterialDanioFormService {
  createMaterialDanioFormGroup(materialDanio: MaterialDanioFormGroupInput = { id: null }): MaterialDanioFormGroup {
    const materialDanioRawValue = {
      ...this.getFormDefaults(),
      ...materialDanio,
    };
    return new FormGroup<MaterialDanioFormGroupContent>({
      id: new FormControl(
        { value: materialDanioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      codigo: new FormControl(materialDanioRawValue.codigo),
      cantidadPedida: new FormControl(materialDanioRawValue.cantidadPedida),
      observacion: new FormControl(materialDanioRawValue.observacion),
    });
  }

  getMaterialDanio(form: MaterialDanioFormGroup): IMaterialDanio | NewMaterialDanio {
    return form.getRawValue() as IMaterialDanio | NewMaterialDanio;
  }

  resetForm(form: MaterialDanioFormGroup, materialDanio: MaterialDanioFormGroupInput): void {
    const materialDanioRawValue = { ...this.getFormDefaults(), ...materialDanio };
    form.reset(
      {
        ...materialDanioRawValue,
        id: { value: materialDanioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MaterialDanioFormDefaults {
    return {
      id: null,
    };
  }
}
