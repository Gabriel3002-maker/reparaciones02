import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMaterialReporteControl } from '../material-reporte-control.model';
import { MaterialReporteControlService } from '../service/material-reporte-control.service';
import { MaterialReporteControlFormService, MaterialReporteControlFormGroup } from './material-reporte-control-form.service';

@Component({
  standalone: true,
  selector: 'jhi-material-reporte-control-update',
  templateUrl: './material-reporte-control-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MaterialReporteControlUpdateComponent implements OnInit {
  isSaving = false;
  materialReporteControl: IMaterialReporteControl | null = null;

  editForm: MaterialReporteControlFormGroup = this.materialReporteControlFormService.createMaterialReporteControlFormGroup();

  constructor(
    protected materialReporteControlService: MaterialReporteControlService,
    protected materialReporteControlFormService: MaterialReporteControlFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ materialReporteControl }) => {
      this.materialReporteControl = materialReporteControl;
      if (materialReporteControl) {
        this.updateForm(materialReporteControl);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const materialReporteControl = this.materialReporteControlFormService.getMaterialReporteControl(this.editForm);
    if (materialReporteControl.id !== null) {
      this.subscribeToSaveResponse(this.materialReporteControlService.update(materialReporteControl));
    } else {
      this.subscribeToSaveResponse(this.materialReporteControlService.create(materialReporteControl));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaterialReporteControl>>): void {
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

  protected updateForm(materialReporteControl: IMaterialReporteControl): void {
    this.materialReporteControl = materialReporteControl;
    this.materialReporteControlFormService.resetForm(this.editForm, materialReporteControl);
  }
}
