import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICatalogoItem } from 'app/entities/catalogo-item/catalogo-item.model';
import { CatalogoItemService } from 'app/entities/catalogo-item/service/catalogo-item.service';
import { IMachinery } from '../machinery.model';
import { MachineryService } from '../service/machinery.service';
import { MachineryFormService, MachineryFormGroup } from './machinery-form.service';

@Component({
  standalone: true,
  selector: 'jhi-machinery-update',
  templateUrl: './machinery-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MachineryUpdateComponent implements OnInit {
  isSaving = false;
  machinery: IMachinery | null = null;

  catalogoItemsSharedCollection: ICatalogoItem[] = [];

  editForm: MachineryFormGroup = this.machineryFormService.createMachineryFormGroup();

  constructor(
    protected machineryService: MachineryService,
    protected machineryFormService: MachineryFormService,
    protected catalogoItemService: CatalogoItemService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCatalogoItem = (o1: ICatalogoItem | null, o2: ICatalogoItem | null): boolean =>
    this.catalogoItemService.compareCatalogoItem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ machinery }) => {
      this.machinery = machinery;
      if (machinery) {
        this.updateForm(machinery);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const machinery = this.machineryFormService.getMachinery(this.editForm);
    if (machinery.id !== null) {
      this.subscribeToSaveResponse(this.machineryService.update(machinery));
    } else {
      this.subscribeToSaveResponse(this.machineryService.create(machinery));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMachinery>>): void {
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

  protected updateForm(machinery: IMachinery): void {
    this.machinery = machinery;
    this.machineryFormService.resetForm(this.editForm, machinery);

    this.catalogoItemsSharedCollection = this.catalogoItemService.addCatalogoItemToCollectionIfMissing<ICatalogoItem>(
      this.catalogoItemsSharedCollection,
      machinery.nombre,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.catalogoItemService
      .query()
      .pipe(map((res: HttpResponse<ICatalogoItem[]>) => res.body ?? []))
      .pipe(
        map((catalogoItems: ICatalogoItem[]) =>
          this.catalogoItemService.addCatalogoItemToCollectionIfMissing<ICatalogoItem>(catalogoItems, this.machinery?.nombre),
        ),
      )
      .subscribe((catalogoItems: ICatalogoItem[]) => (this.catalogoItemsSharedCollection = catalogoItems));
  }
}
