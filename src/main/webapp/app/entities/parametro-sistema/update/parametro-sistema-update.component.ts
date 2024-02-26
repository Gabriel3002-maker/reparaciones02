import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IParametroSistema } from '../parametro-sistema.model';
import { ParametroSistemaService } from '../service/parametro-sistema.service';
import { ParametroSistemaFormService, ParametroSistemaFormGroup } from './parametro-sistema-form.service';

@Component({
  standalone: true,
  selector: 'jhi-parametro-sistema-update',
  templateUrl: './parametro-sistema-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ParametroSistemaUpdateComponent implements OnInit {
  isSaving = false;
  parametroSistema: IParametroSistema | null = null;

  editForm: ParametroSistemaFormGroup = this.parametroSistemaFormService.createParametroSistemaFormGroup();

  constructor(
    protected parametroSistemaService: ParametroSistemaService,
    protected parametroSistemaFormService: ParametroSistemaFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parametroSistema }) => {
      this.parametroSistema = parametroSistema;
      if (parametroSistema) {
        this.updateForm(parametroSistema);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const parametroSistema = this.parametroSistemaFormService.getParametroSistema(this.editForm);
    if (parametroSistema.id !== null) {
      this.subscribeToSaveResponse(this.parametroSistemaService.update(parametroSistema));
    } else {
      this.subscribeToSaveResponse(this.parametroSistemaService.create(parametroSistema));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParametroSistema>>): void {
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

  protected updateForm(parametroSistema: IParametroSistema): void {
    this.parametroSistema = parametroSistema;
    this.parametroSistemaFormService.resetForm(this.editForm, parametroSistema);
  }
}
