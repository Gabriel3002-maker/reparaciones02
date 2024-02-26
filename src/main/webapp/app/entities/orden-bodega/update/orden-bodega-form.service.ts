import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOrdenBodega, NewOrdenBodega } from '../orden-bodega.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrdenBodega for edit and NewOrdenBodegaFormGroupInput for create.
 */
type OrdenBodegaFormGroupInput = IOrdenBodega | PartialWithRequiredKeyOf<NewOrdenBodega>;

type OrdenBodegaFormDefaults = Pick<NewOrdenBodega, 'id'>;

type OrdenBodegaFormGroupContent = {
  id: FormControl<IOrdenBodega['id'] | NewOrdenBodega['id']>;
  codigo: FormControl<IOrdenBodega['codigo']>;
  detalleNecesidad: FormControl<IOrdenBodega['detalleNecesidad']>;
  fecha: FormControl<IOrdenBodega['fecha']>;
  receptor: FormControl<IOrdenBodega['receptor']>;
};

export type OrdenBodegaFormGroup = FormGroup<OrdenBodegaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrdenBodegaFormService {
  createOrdenBodegaFormGroup(ordenBodega: OrdenBodegaFormGroupInput = { id: null }): OrdenBodegaFormGroup {
    const ordenBodegaRawValue = {
      ...this.getFormDefaults(),
      ...ordenBodega,
    };
    return new FormGroup<OrdenBodegaFormGroupContent>({
      id: new FormControl(
        { value: ordenBodegaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      codigo: new FormControl(ordenBodegaRawValue.codigo),
      detalleNecesidad: new FormControl(ordenBodegaRawValue.detalleNecesidad),
      fecha: new FormControl(ordenBodegaRawValue.fecha),
      receptor: new FormControl(ordenBodegaRawValue.receptor),
    });
  }

  getOrdenBodega(form: OrdenBodegaFormGroup): IOrdenBodega | NewOrdenBodega {
    return form.getRawValue() as IOrdenBodega | NewOrdenBodega;
  }

  resetForm(form: OrdenBodegaFormGroup, ordenBodega: OrdenBodegaFormGroupInput): void {
    const ordenBodegaRawValue = { ...this.getFormDefaults(), ...ordenBodega };
    form.reset(
      {
        ...ordenBodegaRawValue,
        id: { value: ordenBodegaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OrdenBodegaFormDefaults {
    return {
      id: null,
    };
  }
}
