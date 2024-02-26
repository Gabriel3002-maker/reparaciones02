import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICatalogoItem } from 'app/entities/catalogo-item/catalogo-item.model';
import { CatalogoItemService } from 'app/entities/catalogo-item/service/catalogo-item.service';
import { IMaterialDanio } from 'app/entities/material-danio/material-danio.model';
import { MaterialDanioService } from 'app/entities/material-danio/service/material-danio.service';
import { IMaterialReporteControl } from 'app/entities/material-reporte-control/material-reporte-control.model';
import { MaterialReporteControlService } from 'app/entities/material-reporte-control/service/material-reporte-control.service';
import { MaterialService } from '../service/material.service';
import { IMaterial } from '../material.model';
import { MaterialFormService, MaterialFormGroup } from './material-form.service';

@Component({
  standalone: true,
  selector: 'jhi-material-update',
  templateUrl: './material-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MaterialUpdateComponent implements OnInit {
  isSaving = false;
  material: IMaterial | null = null;

  catalogoItemsSharedCollection: ICatalogoItem[] = [];
  materialDaniosSharedCollection: IMaterialDanio[] = [];
  materialReporteControlsSharedCollection: IMaterialReporteControl[] = [];

  editForm: MaterialFormGroup = this.materialFormService.createMaterialFormGroup();

  constructor(
    protected materialService: MaterialService,
    protected materialFormService: MaterialFormService,
    protected catalogoItemService: CatalogoItemService,
    protected materialDanioService: MaterialDanioService,
    protected materialReporteControlService: MaterialReporteControlService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCatalogoItem = (o1: ICatalogoItem | null, o2: ICatalogoItem | null): boolean =>
    this.catalogoItemService.compareCatalogoItem(o1, o2);

  compareMaterialDanio = (o1: IMaterialDanio | null, o2: IMaterialDanio | null): boolean =>
    this.materialDanioService.compareMaterialDanio(o1, o2);

  compareMaterialReporteControl = (o1: IMaterialReporteControl | null, o2: IMaterialReporteControl | null): boolean =>
    this.materialReporteControlService.compareMaterialReporteControl(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ material }) => {
      this.material = material;
      if (material) {
        this.updateForm(material);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const material = this.materialFormService.getMaterial(this.editForm);
    if (material.id !== null) {
      this.subscribeToSaveResponse(this.materialService.update(material));
    } else {
      this.subscribeToSaveResponse(this.materialService.create(material));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaterial>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(material: IMaterial): void {
    this.material = material;
    this.materialFormService.resetForm(this.editForm, material);

    this.catalogoItemsSharedCollection = this.catalogoItemService.addCatalogoItemToCollectionIfMissing<ICatalogoItem>(
      this.catalogoItemsSharedCollection,
      material.categoria,
    );
    this.materialDaniosSharedCollection = this.materialDanioService.addMaterialDanioToCollectionIfMissing<IMaterialDanio>(
      this.materialDaniosSharedCollection,
      material.materialDanio,
    );
    this.materialReporteControlsSharedCollection =
      this.materialReporteControlService.addMaterialReporteControlToCollectionIfMissing<IMaterialReporteControl>(
        this.materialReporteControlsSharedCollection,
        material.materialReporteControl,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.catalogoItemService
      .query()
      .pipe(map((res: HttpResponse<ICatalogoItem[]>) => res.body ?? []))
      .pipe(
        map((catalogoItems: ICatalogoItem[]) =>
          this.catalogoItemService.addCatalogoItemToCollectionIfMissing<ICatalogoItem>(catalogoItems, this.material?.categoria),
        ),
      )
      .subscribe((catalogoItems: ICatalogoItem[]) => (this.catalogoItemsSharedCollection = catalogoItems));

    this.materialDanioService
      .query()
      .pipe(map((res: HttpResponse<IMaterialDanio[]>) => res.body ?? []))
      .pipe(
        map((materialDanios: IMaterialDanio[]) =>
          this.materialDanioService.addMaterialDanioToCollectionIfMissing<IMaterialDanio>(materialDanios, this.material?.materialDanio),
        ),
      )
      .subscribe((materialDanios: IMaterialDanio[]) => (this.materialDaniosSharedCollection = materialDanios));

    this.materialReporteControlService
      .query()
      .pipe(map((res: HttpResponse<IMaterialReporteControl[]>) => res.body ?? []))
      .pipe(
        map((materialReporteControls: IMaterialReporteControl[]) =>
          this.materialReporteControlService.addMaterialReporteControlToCollectionIfMissing<IMaterialReporteControl>(
            materialReporteControls,
            this.material?.materialReporteControl,
          ),
        ),
      )
      .subscribe(
        (materialReporteControls: IMaterialReporteControl[]) => (this.materialReporteControlsSharedCollection = materialReporteControls),
      );
  }
}
