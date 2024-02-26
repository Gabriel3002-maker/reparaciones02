import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';
import { FuncionalidadService } from 'app/entities/funcionalidad/service/funcionalidad.service';
import { IRolFuncionalidad } from '../rol-funcionalidad.model';
import { RolFuncionalidadService } from '../service/rol-funcionalidad.service';
import { RolFuncionalidadFormService, RolFuncionalidadFormGroup } from './rol-funcionalidad-form.service';

@Component({
  standalone: true,
  selector: 'jhi-rol-funcionalidad-update',
  templateUrl: './rol-funcionalidad-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RolFuncionalidadUpdateComponent implements OnInit {
  isSaving = false;
  rolFuncionalidad: IRolFuncionalidad | null = null;

  funcionalidadsSharedCollection: IFuncionalidad[] = [];

  editForm: RolFuncionalidadFormGroup = this.rolFuncionalidadFormService.createRolFuncionalidadFormGroup();

  constructor(
    protected rolFuncionalidadService: RolFuncionalidadService,
    protected rolFuncionalidadFormService: RolFuncionalidadFormService,
    protected funcionalidadService: FuncionalidadService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareFuncionalidad = (o1: IFuncionalidad | null, o2: IFuncionalidad | null): boolean =>
    this.funcionalidadService.compareFuncionalidad(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rolFuncionalidad }) => {
      this.rolFuncionalidad = rolFuncionalidad;
      if (rolFuncionalidad) {
        this.updateForm(rolFuncionalidad);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rolFuncionalidad = this.rolFuncionalidadFormService.getRolFuncionalidad(this.editForm);
    if (rolFuncionalidad.id !== null) {
      this.subscribeToSaveResponse(this.rolFuncionalidadService.update(rolFuncionalidad));
    } else {
      this.subscribeToSaveResponse(this.rolFuncionalidadService.create(rolFuncionalidad));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRolFuncionalidad>>): void {
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

  protected updateForm(rolFuncionalidad: IRolFuncionalidad): void {
    this.rolFuncionalidad = rolFuncionalidad;
    this.rolFuncionalidadFormService.resetForm(this.editForm, rolFuncionalidad);

    this.funcionalidadsSharedCollection = this.funcionalidadService.addFuncionalidadToCollectionIfMissing<IFuncionalidad>(
      this.funcionalidadsSharedCollection,
      rolFuncionalidad.funcionalidad,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.funcionalidadService
      .query()
      .pipe(map((res: HttpResponse<IFuncionalidad[]>) => res.body ?? []))
      .pipe(
        map((funcionalidads: IFuncionalidad[]) =>
          this.funcionalidadService.addFuncionalidadToCollectionIfMissing<IFuncionalidad>(
            funcionalidads,
            this.rolFuncionalidad?.funcionalidad,
          ),
        ),
      )
      .subscribe((funcionalidads: IFuncionalidad[]) => (this.funcionalidadsSharedCollection = funcionalidads));
  }
}
