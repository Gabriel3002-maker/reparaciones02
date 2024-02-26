import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMachinery, NewMachinery } from '../machinery.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMachinery for edit and NewMachineryFormGroupInput for create.
 */
type MachineryFormGroupInput = IMachinery | PartialWithRequiredKeyOf<NewMachinery>;

type MachineryFormDefaults = Pick<NewMachinery, 'id'>;

type MachineryFormGroupContent = {
  id: FormControl<IMachinery['id'] | NewMachinery['id']>;
  codigo: FormControl<IMachinery['codigo']>;
  descripcion: FormControl<IMachinery['descripcion']>;
  horasTrabajadas: FormControl<IMachinery['horasTrabajadas']>;
  nombre: FormControl<IMachinery['nombre']>;
};

export type MachineryFormGroup = FormGroup<MachineryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MachineryFormService {
  createMachineryFormGroup(machinery: MachineryFormGroupInput = { id: null }): MachineryFormGroup {
    const machineryRawValue = {
      ...this.getFormDefaults(),
      ...machinery,
    };
    return new FormGroup<MachineryFormGroupContent>({
      id: new FormControl(
        { value: machineryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      codigo: new FormControl(machineryRawValue.codigo),
      descripcion: new FormControl(machineryRawValue.descripcion),
      horasTrabajadas: new FormControl(machineryRawValue.horasTrabajadas),
      nombre: new FormControl(machineryRawValue.nombre),
    });
  }

  getMachinery(form: MachineryFormGroup): IMachinery | NewMachinery {
    return form.getRawValue() as IMachinery | NewMachinery;
  }

  resetForm(form: MachineryFormGroup, machinery: MachineryFormGroupInput): void {
    const machineryRawValue = { ...this.getFormDefaults(), ...machinery };
    form.reset(
      {
        ...machineryRawValue,
        id: { value: machineryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MachineryFormDefaults {
    return {
      id: null,
    };
  }
}
