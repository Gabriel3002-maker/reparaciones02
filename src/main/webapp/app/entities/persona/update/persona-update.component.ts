import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICatalogoItem } from 'app/entities/catalogo-item/catalogo-item.model';
import { CatalogoItemService } from 'app/entities/catalogo-item/service/catalogo-item.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { PersonaService } from '../service/persona.service';
import { IPersona } from '../persona.model';
import { PersonaFormService, PersonaFormGroup } from './persona-form.service';

@Component({
  standalone: true,
  selector: 'jhi-persona-update',
  templateUrl: './persona-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PersonaUpdateComponent implements OnInit {
  isSaving = false;
  persona: IPersona | null = null;

  catalogoItemsSharedCollection: ICatalogoItem[] = [];
  usersSharedCollection: IUser[] = [];

  editForm: PersonaFormGroup = this.personaFormService.createPersonaFormGroup();

  constructor(
    protected personaService: PersonaService,
    protected personaFormService: PersonaFormService,
    protected catalogoItemService: CatalogoItemService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCatalogoItem = (o1: ICatalogoItem | null, o2: ICatalogoItem | null): boolean =>
    this.catalogoItemService.compareCatalogoItem(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ persona }) => {
      this.persona = persona;
      if (persona) {
        this.updateForm(persona);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const persona = this.personaFormService.getPersona(this.editForm);
    if (persona.id !== null) {
      this.subscribeToSaveResponse(this.personaService.update(persona));
    } else {
      this.subscribeToSaveResponse(this.personaService.create(persona));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersona>>): void {
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

  protected updateForm(persona: IPersona): void {
    this.persona = persona;
    this.personaFormService.resetForm(this.editForm, persona);

    this.catalogoItemsSharedCollection = this.catalogoItemService.addCatalogoItemToCollectionIfMissing<ICatalogoItem>(
      this.catalogoItemsSharedCollection,
      persona.tipoIdentificacion,
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, persona.usuario);
  }

  protected loadRelationshipsOptions(): void {
    this.catalogoItemService
      .query()
      .pipe(map((res: HttpResponse<ICatalogoItem[]>) => res.body ?? []))
      .pipe(
        map((catalogoItems: ICatalogoItem[]) =>
          this.catalogoItemService.addCatalogoItemToCollectionIfMissing<ICatalogoItem>(catalogoItems, this.persona?.tipoIdentificacion),
        ),
      )
      .subscribe((catalogoItems: ICatalogoItem[]) => (this.catalogoItemsSharedCollection = catalogoItems));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.persona?.usuario)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
