import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPersona } from 'app/entities/persona/persona.model';
import { PersonaService } from 'app/entities/persona/service/persona.service';
import { IOrdenBodega } from '../orden-bodega.model';
import { OrdenBodegaService } from '../service/orden-bodega.service';
import { OrdenBodegaFormService, OrdenBodegaFormGroup } from './orden-bodega-form.service';

@Component({
  standalone: true,
  selector: 'jhi-orden-bodega-update',
  templateUrl: './orden-bodega-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrdenBodegaUpdateComponent implements OnInit {
  isSaving = false;
  ordenBodega: IOrdenBodega | null = null;

  personasSharedCollection: IPersona[] = [];

  editForm: OrdenBodegaFormGroup = this.ordenBodegaFormService.createOrdenBodegaFormGroup();

  constructor(
    protected ordenBodegaService: OrdenBodegaService,
    protected ordenBodegaFormService: OrdenBodegaFormService,
    protected personaService: PersonaService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  comparePersona = (o1: IPersona | null, o2: IPersona | null): boolean => this.personaService.comparePersona(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ordenBodega }) => {
      this.ordenBodega = ordenBodega;
      if (ordenBodega) {
        this.updateForm(ordenBodega);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ordenBodega = this.ordenBodegaFormService.getOrdenBodega(this.editForm);
    if (ordenBodega.id !== null) {
      this.subscribeToSaveResponse(this.ordenBodegaService.update(ordenBodega));
    } else {
      this.subscribeToSaveResponse(this.ordenBodegaService.create(ordenBodega));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrdenBodega>>): void {
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

  protected updateForm(ordenBodega: IOrdenBodega): void {
    this.ordenBodega = ordenBodega;
    this.ordenBodegaFormService.resetForm(this.editForm, ordenBodega);

    this.personasSharedCollection = this.personaService.addPersonaToCollectionIfMissing<IPersona>(
      this.personasSharedCollection,
      ordenBodega.receptor,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personaService
      .query()
      .pipe(map((res: HttpResponse<IPersona[]>) => res.body ?? []))
      .pipe(
        map((personas: IPersona[]) => this.personaService.addPersonaToCollectionIfMissing<IPersona>(personas, this.ordenBodega?.receptor)),
      )
      .subscribe((personas: IPersona[]) => (this.personasSharedCollection = personas));
  }
}
