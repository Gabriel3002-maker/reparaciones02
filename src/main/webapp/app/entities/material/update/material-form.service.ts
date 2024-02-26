import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMaterial, NewMaterial } from '../material.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMaterial for edit and NewMaterialFormGroupInput for create.
 */
type MaterialFormGroupInput = IMaterial | PartialWithRequiredKeyOf<NewMaterial>;

type MaterialFormDefaults = Pick<NewMaterial, 'id' | 'activo'>;

type MaterialFormGroupContent = {
  id: FormControl<IMaterial['id'] | NewMaterial['id']>;
  codigo: FormControl<IMaterial['codigo']>;
  nombre: FormControl<IMaterial['nombre']>;
  valorUnitario: FormControl<IMaterial['valorUnitario']>;
  stock: FormControl<IMaterial['stock']>;
  activo: FormControl<IMaterial['activo']>;
  descripcion: FormControl<IMaterial['descripcion']>;
  creadoPor: FormControl<IMaterial['creadoPor']>;
  fechaCreacion: FormControl<IMaterial['fechaCreacion']>;
  actualizadoPor: FormControl<IMaterial['actualizadoPor']>;
  fechaModificacion: FormControl<IMaterial['fechaModificacion']>;
  categoria: FormControl<IMaterial['categoria']>;
  materialDanio: FormControl<IMaterial['materialDanio']>;
  materialReporteControl: FormControl<IMaterial['materialReporteControl']>;
};

export type MaterialFormGroup = FormGroup<MaterialFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MaterialFormService {
  createMaterialFormGroup(material: MaterialFormGroupInput = { id: null }): MaterialFormGroup {
    const materialRawValue = {
      ...this.getFormDefaults(),
      ...material,
    };
    return new FormGroup<MaterialFormGroupContent>({
      id: new FormControl(
        { value: materialRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      codigo: new FormControl(materialRawValue.codigo),
      nombre: new FormControl(materialRawValue.nombre, {
        validators: [Validators.required],
      }),
      valorUnitario: new FormControl(materialRawValue.valorUnitario, {
        validators: [Validators.required],
      }),
      stock: new FormControl(materialRawValue.stock),
      activo: new FormControl(materialRawValue.activo),
      descripcion: new FormControl(materialRawValue.descripcion),
      creadoPor: new FormControl(materialRawValue.creadoPor),
      fechaCreacion: new FormControl(materialRawValue.fechaCreacion),
      actualizadoPor: new FormControl(materialRawValue.actualizadoPor),
      fechaModificacion: new FormControl(materialRawValue.fechaModificacion),
      categoria: new FormControl(materialRawValue.categoria, {
        validators: [Validators.required],
      }),
      materialDanio: new FormControl(materialRawValue.materialDanio),
      materialReporteControl: new FormControl(materialRawValue.materialReporteControl),
    });
  }

  getMaterial(form: MaterialFormGroup): IMaterial | NewMaterial {
    return form.getRawValue() as IMaterial | NewMaterial;
  }

  resetForm(form: MaterialFormGroup, material: MaterialFormGroupInput): void {
    const materialRawValue = { ...this.getFormDefaults(), ...material };
    form.reset(
      {
        ...materialRawValue,
        id: { value: materialRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MaterialFormDefaults {
    return {
      id: null,
      activo: false,
    };
  }
}
