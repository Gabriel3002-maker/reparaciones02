import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMaterialReporteControl } from 'app/entities/material-reporte-control/material-reporte-control.model';
import { MaterialReporteControlService } from 'app/entities/material-reporte-control/service/material-reporte-control.service';
import { IDetalleReporteDanio } from '../detalle-reporte-danio.model';
import { DetalleReporteDanioService } from '../service/detalle-reporte-danio.service';
import { DetalleReporteDanioFormService, DetalleReporteDanioFormGroup } from './detalle-reporte-danio-form.service';

@Component({
  standalone: true,
  selector: 'jhi-detalle-reporte-danio-update',
  templateUrl: './detalle-reporte-danio-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DetalleReporteDanioUpdateComponent implements OnInit {
  isSaving = false;
  detalleReporteDanio: IDetalleReporteDanio | null = null;

  materialReporteControlsSharedCollection: IMaterialReporteControl[] = [];

  editForm: DetalleReporteDanioFormGroup = this.detalleReporteDanioFormService.createDetalleReporteDanioFormGroup();

  constructor(
    protected detalleReporteDanioService: DetalleReporteDanioService,
    protected detalleReporteDanioFormService: DetalleReporteDanioFormService,
    protected materialReporteControlService: MaterialReporteControlService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareMaterialReporteControl = (o1: IMaterialReporteControl | null, o2: IMaterialReporteControl | null): boolean =>
    this.materialReporteControlService.compareMaterialReporteControl(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detalleReporteDanio }) => {
      this.detalleReporteDanio = detalleReporteDanio;
      if (detalleReporteDanio) {
        this.updateForm(detalleReporteDanio);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const detalleReporteDanio = this.detalleReporteDanioFormService.getDetalleReporteDanio(this.editForm);
    if (detalleReporteDanio.id !== null) {
      this.subscribeToSaveResponse(this.detalleReporteDanioService.update(detalleReporteDanio));
    } else {
      this.subscribeToSaveResponse(this.detalleReporteDanioService.create(detalleReporteDanio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDetalleReporteDanio>>): void {
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

  protected updateForm(detalleReporteDanio: IDetalleReporteDanio): void {
    this.detalleReporteDanio = detalleReporteDanio;
    this.detalleReporteDanioFormService.resetForm(this.editForm, detalleReporteDanio);

    this.materialReporteControlsSharedCollection =
      this.materialReporteControlService.addMaterialReporteControlToCollectionIfMissing<IMaterialReporteControl>(
        this.materialReporteControlsSharedCollection,
        detalleReporteDanio.materialReporte,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.materialReporteControlService
      .query()
      .pipe(map((res: HttpResponse<IMaterialReporteControl[]>) => res.body ?? []))
      .pipe(
        map((materialReporteControls: IMaterialReporteControl[]) =>
          this.materialReporteControlService.addMaterialReporteControlToCollectionIfMissing<IMaterialReporteControl>(
            materialReporteControls,
            this.detalleReporteDanio?.materialReporte,
          ),
        ),
      )
      .subscribe(
        (materialReporteControls: IMaterialReporteControl[]) => (this.materialReporteControlsSharedCollection = materialReporteControls),
      );
  }
}
