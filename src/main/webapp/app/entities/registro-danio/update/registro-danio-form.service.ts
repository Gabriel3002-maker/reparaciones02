import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRegistroDanio, NewRegistroDanio } from '../registro-danio.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRegistroDanio for edit and NewRegistroDanioFormGroupInput for create.
 */
type RegistroDanioFormGroupInput = IRegistroDanio | PartialWithRequiredKeyOf<NewRegistroDanio>;

type RegistroDanioFormDefaults = Pick<NewRegistroDanio, 'id'>;

type RegistroDanioFormGroupContent = {
  id: FormControl<IRegistroDanio['id'] | NewRegistroDanio['id']>;
  codigo: FormControl<IRegistroDanio['codigo']>;
  fecha: FormControl<IRegistroDanio['fecha']>;
  fechaInicio: FormControl<IRegistroDanio['fechaInicio']>;
  fechaFin: FormControl<IRegistroDanio['fechaFin']>;
  direccion: FormControl<IRegistroDanio['direccion']>;
  parroquia: FormControl<IRegistroDanio['parroquia']>;
  barrio: FormControl<IRegistroDanio['barrio']>;
  detalleDanio: FormControl<IRegistroDanio['detalleDanio']>;
};

export type RegistroDanioFormGroup = FormGroup<RegistroDanioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RegistroDanioFormService {
  createRegistroDanioFormGroup(registroDanio: RegistroDanioFormGroupInput = { id: null }): RegistroDanioFormGroup {
    const registroDanioRawValue = {
      ...this.getFormDefaults(),
      ...registroDanio,
    };
    return new FormGroup<RegistroDanioFormGroupContent>({
      id: new FormControl(
        { value: registroDanioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      codigo: new FormControl(registroDanioRawValue.codigo, {
        validators: [Validators.required],
      }),
      fecha: new FormControl(registroDanioRawValue.fecha),
      fechaInicio: new FormControl(registroDanioRawValue.fechaInicio),
      fechaFin: new FormControl(registroDanioRawValue.fechaFin),
      direccion: new FormControl(registroDanioRawValue.direccion),
      parroquia: new FormControl(registroDanioRawValue.parroquia),
      barrio: new FormControl(registroDanioRawValue.barrio),
      detalleDanio: new FormControl(registroDanioRawValue.detalleDanio),
    });
  }

  getRegistroDanio(form: RegistroDanioFormGroup): IRegistroDanio | NewRegistroDanio {
    return form.getRawValue() as IRegistroDanio | NewRegistroDanio;
  }

  resetForm(form: RegistroDanioFormGroup, registroDanio: RegistroDanioFormGroupInput): void {
    const registroDanioRawValue = { ...this.getFormDefaults(), ...registroDanio };
    form.reset(
      {
        ...registroDanioRawValue,
        id: { value: registroDanioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RegistroDanioFormDefaults {
    return {
      id: null,
    };
  }
}
