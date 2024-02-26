import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICatalogoItem, NewCatalogoItem } from '../catalogo-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICatalogoItem for edit and NewCatalogoItemFormGroupInput for create.
 */
type CatalogoItemFormGroupInput = ICatalogoItem | PartialWithRequiredKeyOf<NewCatalogoItem>;

type CatalogoItemFormDefaults = Pick<NewCatalogoItem, 'id' | 'activo'>;

type CatalogoItemFormGroupContent = {
  id: FormControl<ICatalogoItem['id'] | NewCatalogoItem['id']>;
  nombre: FormControl<ICatalogoItem['nombre']>;
  codigo: FormControl<ICatalogoItem['codigo']>;
  descripcion: FormControl<ICatalogoItem['descripcion']>;
  catalogoCodigo: FormControl<ICatalogoItem['catalogoCodigo']>;
  activo: FormControl<ICatalogoItem['activo']>;
  catalogo: FormControl<ICatalogoItem['catalogo']>;
};

export type CatalogoItemFormGroup = FormGroup<CatalogoItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CatalogoItemFormService {
  createCatalogoItemFormGroup(catalogoItem: CatalogoItemFormGroupInput = { id: null }): CatalogoItemFormGroup {
    const catalogoItemRawValue = {
      ...this.getFormDefaults(),
      ...catalogoItem,
    };
    return new FormGroup<CatalogoItemFormGroupContent>({
      id: new FormControl(
        { value: catalogoItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(catalogoItemRawValue.nombre, {
        validators: [Validators.required],
      }),
      codigo: new FormControl(catalogoItemRawValue.codigo, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(catalogoItemRawValue.descripcion),
      catalogoCodigo: new FormControl(catalogoItemRawValue.catalogoCodigo, {
        validators: [Validators.required],
      }),
      activo: new FormControl(catalogoItemRawValue.activo),
      catalogo: new FormControl(catalogoItemRawValue.catalogo),
    });
  }

  getCatalogoItem(form: CatalogoItemFormGroup): ICatalogoItem | NewCatalogoItem {
    return form.getRawValue() as ICatalogoItem | NewCatalogoItem;
  }

  resetForm(form: CatalogoItemFormGroup, catalogoItem: CatalogoItemFormGroupInput): void {
    const catalogoItemRawValue = { ...this.getFormDefaults(), ...catalogoItem };
    form.reset(
      {
        ...catalogoItemRawValue,
        id: { value: catalogoItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CatalogoItemFormDefaults {
    return {
      id: null,
      activo: false,
    };
  }
}
