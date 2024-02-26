import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDetalleDanio } from 'app/entities/detalle-danio/detalle-danio.model';
import { DetalleDanioService } from 'app/entities/detalle-danio/service/detalle-danio.service';
import { IRegistroDanio } from '../registro-danio.model';
import { RegistroDanioService } from '../service/registro-danio.service';
import { RegistroDanioFormService, RegistroDanioFormGroup } from './registro-danio-form.service';

@Component({
  standalone: true,
  selector: 'jhi-registro-danio-update',
  templateUrl: './registro-danio-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RegistroDanioUpdateComponent implements OnInit {
  isSaving = false;
  registroDanio: IRegistroDanio | null = null;

  detalleDaniosSharedCollection: IDetalleDanio[] = [];

  editForm: RegistroDanioFormGroup = this.registroDanioFormService.createRegistroDanioFormGroup();

  constructor(
    protected registroDanioService: RegistroDanioService,
    protected registroDanioFormService: RegistroDanioFormService,
    protected detalleDanioService: DetalleDanioService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareDetalleDanio = (o1: IDetalleDanio | null, o2: IDetalleDanio | null): boolean =>
    this.detalleDanioService.compareDetalleDanio(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ registroDanio }) => {
      this.registroDanio = registroDanio;
      if (registroDanio) {
        this.updateForm(registroDanio);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const registroDanio = this.registroDanioFormService.getRegistroDanio(this.editForm);
    if (registroDanio.id !== null) {
      this.subscribeToSaveResponse(this.registroDanioService.update(registroDanio));
    } else {
      this.subscribeToSaveResponse(this.registroDanioService.create(registroDanio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRegistroDanio>>): void {
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

  protected updateForm(registroDanio: IRegistroDanio): void {
    this.registroDanio = registroDanio;
    this.registroDanioFormService.resetForm(this.editForm, registroDanio);

    this.detalleDaniosSharedCollection = this.detalleDanioService.addDetalleDanioToCollectionIfMissing<IDetalleDanio>(
      this.detalleDaniosSharedCollection,
      registroDanio.detalleDanio,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.detalleDanioService
      .query()
      .pipe(map((res: HttpResponse<IDetalleDanio[]>) => res.body ?? []))
      .pipe(
        map((detalleDanios: IDetalleDanio[]) =>
          this.detalleDanioService.addDetalleDanioToCollectionIfMissing<IDetalleDanio>(detalleDanios, this.registroDanio?.detalleDanio),
        ),
      )
      .subscribe((detalleDanios: IDetalleDanio[]) => (this.detalleDaniosSharedCollection = detalleDanios));
  }
}
