import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IParametroSistema, NewParametroSistema } from '../parametro-sistema.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IParametroSistema for edit and NewParametroSistemaFormGroupInput for create.
 */
type ParametroSistemaFormGroupInput = IParametroSistema | PartialWithRequiredKeyOf<NewParametroSistema>;

type ParametroSistemaFormDefaults = Pick<NewParametroSistema, 'id'>;

type ParametroSistemaFormGroupContent = {
  id: FormControl<IParametroSistema['id'] | NewParametroSistema['id']>;
  nombre: FormControl<IParametroSistema['nombre']>;
  codigo: FormControl<IParametroSistema['codigo']>;
  clase: FormControl<IParametroSistema['clase']>;
  valor: FormControl<IParametroSistema['valor']>;
};

export type ParametroSistemaFormGroup = FormGroup<ParametroSistemaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ParametroSistemaFormService {
  createParametroSistemaFormGroup(parametroSistema: ParametroSistemaFormGroupInput = { id: null }): ParametroSistemaFormGroup {
    const parametroSistemaRawValue = {
      ...this.getFormDefaults(),
      ...parametroSistema,
    };
    return new FormGroup<ParametroSistemaFormGroupContent>({
      id: new FormControl(
        { value: parametroSistemaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(parametroSistemaRawValue.nombre, {
        validators: [Validators.required],
      }),
      codigo: new FormControl(parametroSistemaRawValue.codigo, {
        validators: [Validators.required],
      }),
      clase: new FormControl(parametroSistemaRawValue.clase, {
        validators: [Validators.required],
      }),
      valor: new FormControl(parametroSistemaRawValue.valor, {
        validators: [Validators.required],
      }),
    });
  }

  getParametroSistema(form: ParametroSistemaFormGroup): IParametroSistema | NewParametroSistema {
    return form.getRawValue() as IParametroSistema | NewParametroSistema;
  }

  resetForm(form: ParametroSistemaFormGroup, parametroSistema: ParametroSistemaFormGroupInput): void {
    const parametroSistemaRawValue = { ...this.getFormDefaults(), ...parametroSistema };
    form.reset(
      {
        ...parametroSistemaRawValue,
        id: { value: parametroSistemaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ParametroSistemaFormDefaults {
    return {
      id: null,
    };
  }
}
