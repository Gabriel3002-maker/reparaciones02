import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRolFuncionalidad, NewRolFuncionalidad } from '../rol-funcionalidad.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRolFuncionalidad for edit and NewRolFuncionalidadFormGroupInput for create.
 */
type RolFuncionalidadFormGroupInput = IRolFuncionalidad | PartialWithRequiredKeyOf<NewRolFuncionalidad>;

type RolFuncionalidadFormDefaults = Pick<NewRolFuncionalidad, 'id' | 'activo'>;

type RolFuncionalidadFormGroupContent = {
  id: FormControl<IRolFuncionalidad['id'] | NewRolFuncionalidad['id']>;
  rol: FormControl<IRolFuncionalidad['rol']>;
  activo: FormControl<IRolFuncionalidad['activo']>;
  prioridad: FormControl<IRolFuncionalidad['prioridad']>;
  funcionalidad: FormControl<IRolFuncionalidad['funcionalidad']>;
};

export type RolFuncionalidadFormGroup = FormGroup<RolFuncionalidadFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RolFuncionalidadFormService {
  createRolFuncionalidadFormGroup(rolFuncionalidad: RolFuncionalidadFormGroupInput = { id: null }): RolFuncionalidadFormGroup {
    const rolFuncionalidadRawValue = {
      ...this.getFormDefaults(),
      ...rolFuncionalidad,
    };
    return new FormGroup<RolFuncionalidadFormGroupContent>({
      id: new FormControl(
        { value: rolFuncionalidadRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      rol: new FormControl(rolFuncionalidadRawValue.rol, {
        validators: [Validators.required],
      }),
      activo: new FormControl(rolFuncionalidadRawValue.activo),
      prioridad: new FormControl(rolFuncionalidadRawValue.prioridad, {
        validators: [Validators.required],
      }),
      funcionalidad: new FormControl(rolFuncionalidadRawValue.funcionalidad, {
        validators: [Validators.required],
      }),
    });
  }

  getRolFuncionalidad(form: RolFuncionalidadFormGroup): IRolFuncionalidad | NewRolFuncionalidad {
    return form.getRawValue() as IRolFuncionalidad | NewRolFuncionalidad;
  }

  resetForm(form: RolFuncionalidadFormGroup, rolFuncionalidad: RolFuncionalidadFormGroupInput): void {
    const rolFuncionalidadRawValue = { ...this.getFormDefaults(), ...rolFuncionalidad };
    form.reset(
      {
        ...rolFuncionalidadRawValue,
        id: { value: rolFuncionalidadRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RolFuncionalidadFormDefaults {
    return {
      id: null,
      activo: false,
    };
  }
}
