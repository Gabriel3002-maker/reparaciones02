import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMaterialDanio } from '../material-danio.model';
import { MaterialDanioService } from '../service/material-danio.service';
import { MaterialDanioFormService, MaterialDanioFormGroup } from './material-danio-form.service';

@Component({
  standalone: true,
  selector: 'jhi-material-danio-update',
  templateUrl: './material-danio-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MaterialDanioUpdateComponent implements OnInit {
  isSaving = false;
  materialDanio: IMaterialDanio | null = null;

  editForm: MaterialDanioFormGroup = this.materialDanioFormService.createMaterialDanioFormGroup();

  constructor(
    protected materialDanioService: MaterialDanioService,
    protected materialDanioFormService: MaterialDanioFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ materialDanio }) => {
      this.materialDanio = materialDanio;
      if (materialDanio) {
        this.updateForm(materialDanio);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const materialDanio = this.materialDanioFormService.getMaterialDanio(this.editForm);
    if (materialDanio.id !== null) {
      this.subscribeToSaveResponse(this.materialDanioService.update(materialDanio));
    } else {
      this.subscribeToSaveResponse(this.materialDanioService.create(materialDanio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaterialDanio>>): void {
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

  protected updateForm(materialDanio: IMaterialDanio): void {
    this.materialDanio = materialDanio;
    this.materialDanioFormService.resetForm(this.editForm, materialDanio);
  }
}
