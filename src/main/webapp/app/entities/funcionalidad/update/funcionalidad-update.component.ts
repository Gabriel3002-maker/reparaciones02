import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFuncionalidad } from '../funcionalidad.model';
import { FuncionalidadService } from '../service/funcionalidad.service';
import { FuncionalidadFormService, FuncionalidadFormGroup } from './funcionalidad-form.service';

@Component({
  standalone: true,
  selector: 'jhi-funcionalidad-update',
  templateUrl: './funcionalidad-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FuncionalidadUpdateComponent implements OnInit {
  isSaving = false;
  funcionalidad: IFuncionalidad | null = null;

  funcionalidadsSharedCollection: IFuncionalidad[] = [];

  editForm: FuncionalidadFormGroup = this.funcionalidadFormService.createFuncionalidadFormGroup();

  constructor(
    protected funcionalidadService: FuncionalidadService,
    protected funcionalidadFormService: FuncionalidadFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareFuncionalidad = (o1: IFuncionalidad | null, o2: IFuncionalidad | null): boolean =>
    this.funcionalidadService.compareFuncionalidad(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ funcionalidad }) => {
      this.funcionalidad = funcionalidad;
      if (funcionalidad) {
        this.updateForm(funcionalidad);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const funcionalidad = this.funcionalidadFormService.getFuncionalidad(this.editForm);
    if (funcionalidad.id !== null) {
      this.subscribeToSaveResponse(this.funcionalidadService.update(funcionalidad));
    } else {
      this.subscribeToSaveResponse(this.funcionalidadService.create(funcionalidad));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFuncionalidad>>): void {
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

  protected updateForm(funcionalidad: IFuncionalidad): void {
    this.funcionalidad = funcionalidad;
    this.funcionalidadFormService.resetForm(this.editForm, funcionalidad);

    this.funcionalidadsSharedCollection = this.funcionalidadService.addFuncionalidadToCollectionIfMissing<IFuncionalidad>(
      this.funcionalidadsSharedCollection,
      funcionalidad.padre,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.funcionalidadService
      .query()
      .pipe(map((res: HttpResponse<IFuncionalidad[]>) => res.body ?? []))
      .pipe(
        map((funcionalidads: IFuncionalidad[]) =>
          this.funcionalidadService.addFuncionalidadToCollectionIfMissing<IFuncionalidad>(funcionalidads, this.funcionalidad?.padre),
        ),
      )
      .subscribe((funcionalidads: IFuncionalidad[]) => (this.funcionalidadsSharedCollection = funcionalidads));
  }
}
