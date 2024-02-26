import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPersona, NewPersona } from '../persona.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPersona for edit and NewPersonaFormGroupInput for create.
 */
type PersonaFormGroupInput = IPersona | PartialWithRequiredKeyOf<NewPersona>;

type PersonaFormDefaults = Pick<NewPersona, 'id'>;

type PersonaFormGroupContent = {
  id: FormControl<IPersona['id'] | NewPersona['id']>;
  identificacion: FormControl<IPersona['identificacion']>;
  primerApellido: FormControl<IPersona['primerApellido']>;
  segundoApellido: FormControl<IPersona['segundoApellido']>;
  primerNombre: FormControl<IPersona['primerNombre']>;
  segundoNombre: FormControl<IPersona['segundoNombre']>;
  celular: FormControl<IPersona['celular']>;
  telefonoConvencional: FormControl<IPersona['telefonoConvencional']>;
  correo: FormControl<IPersona['correo']>;
  tipoIdentificacion: FormControl<IPersona['tipoIdentificacion']>;
  usuario: FormControl<IPersona['usuario']>;
};

export type PersonaFormGroup = FormGroup<PersonaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PersonaFormService {
  createPersonaFormGroup(persona: PersonaFormGroupInput = { id: null }): PersonaFormGroup {
    const personaRawValue = {
      ...this.getFormDefaults(),
      ...persona,
    };
    return new FormGroup<PersonaFormGroupContent>({
      id: new FormControl(
        { value: personaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      identificacion: new FormControl(personaRawValue.identificacion, {
        validators: [Validators.required],
      }),
      primerApellido: new FormControl(personaRawValue.primerApellido, {
        validators: [Validators.required],
      }),
      segundoApellido: new FormControl(personaRawValue.segundoApellido),
      primerNombre: new FormControl(personaRawValue.primerNombre, {
        validators: [Validators.required],
      }),
      segundoNombre: new FormControl(personaRawValue.segundoNombre),
      celular: new FormControl(personaRawValue.celular),
      telefonoConvencional: new FormControl(personaRawValue.telefonoConvencional),
      correo: new FormControl(personaRawValue.correo),
      tipoIdentificacion: new FormControl(personaRawValue.tipoIdentificacion, {
        validators: [Validators.required],
      }),
      usuario: new FormControl(personaRawValue.usuario),
    });
  }

  getPersona(form: PersonaFormGroup): IPersona | NewPersona {
    return form.getRawValue() as IPersona | NewPersona;
  }

  resetForm(form: PersonaFormGroup, persona: PersonaFormGroupInput): void {
    const personaRawValue = { ...this.getFormDefaults(), ...persona };
    form.reset(
      {
        ...personaRawValue,
        id: { value: personaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PersonaFormDefaults {
    return {
      id: null,
    };
  }
}
