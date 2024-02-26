import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDetalleDanio, NewDetalleDanio } from '../detalle-danio.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDetalleDanio for edit and NewDetalleDanioFormGroupInput for create.
 */
type DetalleDanioFormGroupInput = IDetalleDanio | PartialWithRequiredKeyOf<NewDetalleDanio>;

type DetalleDanioFormDefaults = Pick<NewDetalleDanio, 'id'>;

type DetalleDanioFormGroupContent = {
  id: FormControl<IDetalleDanio['id'] | NewDetalleDanio['id']>;
  codigo: FormControl<IDetalleDanio['codigo']>;
  descripcionDanio: FormControl<IDetalleDanio['descripcionDanio']>;
  tecnico: FormControl<IDetalleDanio['tecnico']>;
  namePerson: FormControl<IDetalleDanio['namePerson']>;
  direccion: FormControl<IDetalleDanio['direccion']>;
  estadoReparacion: FormControl<IDetalleDanio['estadoReparacion']>;
  observacion: FormControl<IDetalleDanio['observacion']>;
  tipoDanio: FormControl<IDetalleDanio['tipoDanio']>;
  materialDanio: FormControl<IDetalleDanio['materialDanio']>;
  maquinaria: FormControl<IDetalleDanio['maquinaria']>;
  ordenBodega: FormControl<IDetalleDanio['ordenBodega']>;
  detalleReporteDanio: FormControl<IDetalleDanio['detalleReporteDanio']>;
};

export type DetalleDanioFormGroup = FormGroup<DetalleDanioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DetalleDanioFormService {
  createDetalleDanioFormGroup(detalleDanio: DetalleDanioFormGroupInput = { id: null }): DetalleDanioFormGroup {
    const detalleDanioRawValue = {
      ...this.getFormDefaults(),
      ...detalleDanio,
    };
    return new FormGroup<DetalleDanioFormGroupContent>({
      id: new FormControl(
        { value: detalleDanioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      codigo: new FormControl(detalleDanioRawValue.codigo, {
        validators: [Validators.required],
      }),
      descripcionDanio: new FormControl(detalleDanioRawValue.descripcionDanio),
      tecnico: new FormControl(detalleDanioRawValue.tecnico),
      namePerson: new FormControl(detalleDanioRawValue.namePerson),
      direccion: new FormControl(detalleDanioRawValue.direccion),
      estadoReparacion: new FormControl(detalleDanioRawValue.estadoReparacion),
      observacion: new FormControl(detalleDanioRawValue.observacion),
      tipoDanio: new FormControl(detalleDanioRawValue.tipoDanio, {
        validators: [Validators.required],
      }),
      materialDanio: new FormControl(detalleDanioRawValue.materialDanio),
      maquinaria: new FormControl(detalleDanioRawValue.maquinaria),
      ordenBodega: new FormControl(detalleDanioRawValue.ordenBodega),
      detalleReporteDanio: new FormControl(detalleDanioRawValue.detalleReporteDanio),
    });
  }

  getDetalleDanio(form: DetalleDanioFormGroup): IDetalleDanio | NewDetalleDanio {
    return form.getRawValue() as IDetalleDanio | NewDetalleDanio;
  }

  resetForm(form: DetalleDanioFormGroup, detalleDanio: DetalleDanioFormGroupInput): void {
    const detalleDanioRawValue = { ...this.getFormDefaults(), ...detalleDanio };
    form.reset(
      {
        ...detalleDanioRawValue,
        id: { value: detalleDanioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DetalleDanioFormDefaults {
    return {
      id: null,
    };
  }
}
