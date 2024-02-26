import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDetalleReporteDanio, NewDetalleReporteDanio } from '../detalle-reporte-danio.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDetalleReporteDanio for edit and NewDetalleReporteDanioFormGroupInput for create.
 */
type DetalleReporteDanioFormGroupInput = IDetalleReporteDanio | PartialWithRequiredKeyOf<NewDetalleReporteDanio>;

type DetalleReporteDanioFormDefaults = Pick<NewDetalleReporteDanio, 'id'>;

type DetalleReporteDanioFormGroupContent = {
  id: FormControl<IDetalleReporteDanio['id'] | NewDetalleReporteDanio['id']>;
  codigo: FormControl<IDetalleReporteDanio['codigo']>;
  fecha: FormControl<IDetalleReporteDanio['fecha']>;
  contribuyente: FormControl<IDetalleReporteDanio['contribuyente']>;
  direccion: FormControl<IDetalleReporteDanio['direccion']>;
  referencia: FormControl<IDetalleReporteDanio['referencia']>;
  horasTrabajadas: FormControl<IDetalleReporteDanio['horasTrabajadas']>;
  personalResponsable: FormControl<IDetalleReporteDanio['personalResponsable']>;
  materialReporte: FormControl<IDetalleReporteDanio['materialReporte']>;
};

export type DetalleReporteDanioFormGroup = FormGroup<DetalleReporteDanioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DetalleReporteDanioFormService {
  createDetalleReporteDanioFormGroup(detalleReporteDanio: DetalleReporteDanioFormGroupInput = { id: null }): DetalleReporteDanioFormGroup {
    const detalleReporteDanioRawValue = {
      ...this.getFormDefaults(),
      ...detalleReporteDanio,
    };
    return new FormGroup<DetalleReporteDanioFormGroupContent>({
      id: new FormControl(
        { value: detalleReporteDanioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      codigo: new FormControl(detalleReporteDanioRawValue.codigo),
      fecha: new FormControl(detalleReporteDanioRawValue.fecha),
      contribuyente: new FormControl(detalleReporteDanioRawValue.contribuyente),
      direccion: new FormControl(detalleReporteDanioRawValue.direccion),
      referencia: new FormControl(detalleReporteDanioRawValue.referencia),
      horasTrabajadas: new FormControl(detalleReporteDanioRawValue.horasTrabajadas),
      personalResponsable: new FormControl(detalleReporteDanioRawValue.personalResponsable),
      materialReporte: new FormControl(detalleReporteDanioRawValue.materialReporte),
    });
  }

  getDetalleReporteDanio(form: DetalleReporteDanioFormGroup): IDetalleReporteDanio | NewDetalleReporteDanio {
    return form.getRawValue() as IDetalleReporteDanio | NewDetalleReporteDanio;
  }

  resetForm(form: DetalleReporteDanioFormGroup, detalleReporteDanio: DetalleReporteDanioFormGroupInput): void {
    const detalleReporteDanioRawValue = { ...this.getFormDefaults(), ...detalleReporteDanio };
    form.reset(
      {
        ...detalleReporteDanioRawValue,
        id: { value: detalleReporteDanioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DetalleReporteDanioFormDefaults {
    return {
      id: null,
    };
  }
}
