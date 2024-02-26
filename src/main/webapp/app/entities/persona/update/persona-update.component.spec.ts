import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICatalogoItem } from 'app/entities/catalogo-item/catalogo-item.model';
import { CatalogoItemService } from 'app/entities/catalogo-item/service/catalogo-item.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPersona } from '../persona.model';
import { PersonaService } from '../service/persona.service';
import { PersonaFormService } from './persona-form.service';

import { PersonaUpdateComponent } from './persona-update.component';

describe('Persona Management Update Component', () => {
  let comp: PersonaUpdateComponent;
  let fixture: ComponentFixture<PersonaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let personaFormService: PersonaFormService;
  let personaService: PersonaService;
  let catalogoItemService: CatalogoItemService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PersonaUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PersonaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PersonaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    personaFormService = TestBed.inject(PersonaFormService);
    personaService = TestBed.inject(PersonaService);
    catalogoItemService = TestBed.inject(CatalogoItemService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CatalogoItem query and add missing value', () => {
      const persona: IPersona = { id: 456 };
      const tipoIdentificacion: ICatalogoItem = { id: 3781 };
      persona.tipoIdentificacion = tipoIdentificacion;

      const catalogoItemCollection: ICatalogoItem[] = [{ id: 8325 }];
      jest.spyOn(catalogoItemService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogoItemCollection })));
      const additionalCatalogoItems = [tipoIdentificacion];
      const expectedCollection: ICatalogoItem[] = [...additionalCatalogoItems, ...catalogoItemCollection];
      jest.spyOn(catalogoItemService, 'addCatalogoItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ persona });
      comp.ngOnInit();

      expect(catalogoItemService.query).toHaveBeenCalled();
      expect(catalogoItemService.addCatalogoItemToCollectionIfMissing).toHaveBeenCalledWith(
        catalogoItemCollection,
        ...additionalCatalogoItems.map(expect.objectContaining),
      );
      expect(comp.catalogoItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const persona: IPersona = { id: 456 };
      const usuario: IUser = { id: 14275 };
      persona.usuario = usuario;

      const userCollection: IUser[] = [{ id: 2086 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [usuario];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ persona });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const persona: IPersona = { id: 456 };
      const tipoIdentificacion: ICatalogoItem = { id: 8547 };
      persona.tipoIdentificacion = tipoIdentificacion;
      const usuario: IUser = { id: 22568 };
      persona.usuario = usuario;

      activatedRoute.data = of({ persona });
      comp.ngOnInit();

      expect(comp.catalogoItemsSharedCollection).toContain(tipoIdentificacion);
      expect(comp.usersSharedCollection).toContain(usuario);
      expect(comp.persona).toEqual(persona);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersona>>();
      const persona = { id: 123 };
      jest.spyOn(personaFormService, 'getPersona').mockReturnValue(persona);
      jest.spyOn(personaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ persona });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: persona }));
      saveSubject.complete();

      // THEN
      expect(personaFormService.getPersona).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(personaService.update).toHaveBeenCalledWith(expect.objectContaining(persona));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersona>>();
      const persona = { id: 123 };
      jest.spyOn(personaFormService, 'getPersona').mockReturnValue({ id: null });
      jest.spyOn(personaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ persona: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: persona }));
      saveSubject.complete();

      // THEN
      expect(personaFormService.getPersona).toHaveBeenCalled();
      expect(personaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersona>>();
      const persona = { id: 123 };
      jest.spyOn(personaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ persona });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(personaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCatalogoItem', () => {
      it('Should forward to catalogoItemService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(catalogoItemService, 'compareCatalogoItem');
        comp.compareCatalogoItem(entity, entity2);
        expect(catalogoItemService.compareCatalogoItem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
