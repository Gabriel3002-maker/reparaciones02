import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFuncionalidad, NewFuncionalidad } from '../funcionalidad.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFuncionalidad for edit and NewFuncionalidadFormGroupInput for create.
 */
type FuncionalidadFormGroupInput = IFuncionalidad | PartialWithRequiredKeyOf<NewFuncionalidad>;

type FuncionalidadFormDefaults = Pick<NewFuncionalidad, 'id' | 'activo' | 'visible'>;

type FuncionalidadFormGroupContent = {
  id: FormControl<IFuncionalidad['id'] | NewFuncionalidad['id']>;
  nombre: FormControl<IFuncionalidad['nombre']>;
  descripcion: FormControl<IFuncionalidad['descripcion']>;
  url: FormControl<IFuncionalidad['url']>;
  activo: FormControl<IFuncionalidad['activo']>;
  icono: FormControl<IFuncionalidad['icono']>;
  visible: FormControl<IFuncionalidad['visible']>;
  padre: FormControl<IFuncionalidad['padre']>;
};

export type FuncionalidadFormGroup = FormGroup<FuncionalidadFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FuncionalidadFormService {
  createFuncionalidadFormGroup(funcionalidad: FuncionalidadFormGroupInput = { id: null }): FuncionalidadFormGroup {
    const funcionalidadRawValue = {
      ...this.getFormDefaults(),
      ...funcionalidad,
    };
    return new FormGroup<FuncionalidadFormGroupContent>({
      id: new FormControl(
        { value: funcionalidadRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(funcionalidadRawValue.nombre, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(funcionalidadRawValue.descripcion),
      url: new FormControl(funcionalidadRawValue.url, {
        validators: [Validators.maxLength(80)],
      }),
      activo: new FormControl(funcionalidadRawValue.activo, {
        validators: [Validators.required],
      }),
      icono: new FormControl(funcionalidadRawValue.icono),
      visible: new FormControl(funcionalidadRawValue.visible),
      padre: new FormControl(funcionalidadRawValue.padre),
    });
  }

  getFuncionalidad(form: FuncionalidadFormGroup): IFuncionalidad | NewFuncionalidad {
    return form.getRawValue() as IFuncionalidad | NewFuncionalidad;
  }

  resetForm(form: FuncionalidadFormGroup, funcionalidad: FuncionalidadFormGroupInput): void {
    const funcionalidadRawValue = { ...this.getFormDefaults(), ...funcionalidad };
    form.reset(
      {
        ...funcionalidadRawValue,
        id: { value: funcionalidadRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FuncionalidadFormDefaults {
    return {
      id: null,
      activo: false,
      visible: false,
    };
  }
}
