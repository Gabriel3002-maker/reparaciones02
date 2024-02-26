import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICatalogo } from 'app/entities/catalogo/catalogo.model';
import { CatalogoService } from 'app/entities/catalogo/service/catalogo.service';
import { ICatalogoItem } from '../catalogo-item.model';
import { CatalogoItemService } from '../service/catalogo-item.service';
import { CatalogoItemFormService, CatalogoItemFormGroup } from './catalogo-item-form.service';

@Component({
  standalone: true,
  selector: 'jhi-catalogo-item-update',
  templateUrl: './catalogo-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CatalogoItemUpdateComponent implements OnInit {
  isSaving = false;
  catalogoItem: ICatalogoItem | null = null;

  catalogosSharedCollection: ICatalogo[] = [];

  editForm: CatalogoItemFormGroup = this.catalogoItemFormService.createCatalogoItemFormGroup();

  constructor(
    protected catalogoItemService: CatalogoItemService,
    protected catalogoItemFormService: CatalogoItemFormService,
    protected catalogoService: CatalogoService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCatalogo = (o1: ICatalogo | null, o2: ICatalogo | null): boolean => this.catalogoService.compareCatalogo(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogoItem }) => {
      this.catalogoItem = catalogoItem;
      if (catalogoItem) {
        this.updateForm(catalogoItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const catalogoItem = this.catalogoItemFormService.getCatalogoItem(this.editForm);
    if (catalogoItem.id !== null) {
      this.subscribeToSaveResponse(this.catalogoItemService.update(catalogoItem));
    } else {
      this.subscribeToSaveResponse(this.catalogoItemService.create(catalogoItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICatalogoItem>>): void {
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

  protected updateForm(catalogoItem: ICatalogoItem): void {
    this.catalogoItem = catalogoItem;
    this.catalogoItemFormService.resetForm(this.editForm, catalogoItem);

    this.catalogosSharedCollection = this.catalogoService.addCatalogoToCollectionIfMissing<ICatalogo>(
      this.catalogosSharedCollection,
      catalogoItem.catalogo,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.catalogoService
      .query()
      .pipe(map((res: HttpResponse<ICatalogo[]>) => res.body ?? []))
      .pipe(
        map((catalogos: ICatalogo[]) =>
          this.catalogoService.addCatalogoToCollectionIfMissing<ICatalogo>(catalogos, this.catalogoItem?.catalogo),
        ),
      )
      .subscribe((catalogos: ICatalogo[]) => (this.catalogosSharedCollection = catalogos));
  }
}
