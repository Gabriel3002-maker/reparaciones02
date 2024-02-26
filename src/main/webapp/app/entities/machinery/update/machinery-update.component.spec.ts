import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICatalogoItem } from 'app/entities/catalogo-item/catalogo-item.model';
import { CatalogoItemService } from 'app/entities/catalogo-item/service/catalogo-item.service';
import { MachineryService } from '../service/machinery.service';
import { IMachinery } from '../machinery.model';
import { MachineryFormService } from './machinery-form.service';

import { MachineryUpdateComponent } from './machinery-update.component';

describe('Machinery Management Update Component', () => {
  let comp: MachineryUpdateComponent;
  let fixture: ComponentFixture<MachineryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let machineryFormService: MachineryFormService;
  let machineryService: MachineryService;
  let catalogoItemService: CatalogoItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MachineryUpdateComponent],
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
      .overrideTemplate(MachineryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MachineryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    machineryFormService = TestBed.inject(MachineryFormService);
    machineryService = TestBed.inject(MachineryService);
    catalogoItemService = TestBed.inject(CatalogoItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CatalogoItem query and add missing value', () => {
      const machinery: IMachinery = { id: 456 };
      const nombre: ICatalogoItem = { id: 21675 };
      machinery.nombre = nombre;

      const catalogoItemCollection: ICatalogoItem[] = [{ id: 12262 }];
      jest.spyOn(catalogoItemService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogoItemCollection })));
      const additionalCatalogoItems = [nombre];
      const expectedCollection: ICatalogoItem[] = [...additionalCatalogoItems, ...catalogoItemCollection];
      jest.spyOn(catalogoItemService, 'addCatalogoItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ machinery });
      comp.ngOnInit();

      expect(catalogoItemService.query).toHaveBeenCalled();
      expect(catalogoItemService.addCatalogoItemToCollectionIfMissing).toHaveBeenCalledWith(
        catalogoItemCollection,
        ...additionalCatalogoItems.map(expect.objectContaining),
      );
      expect(comp.catalogoItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const machinery: IMachinery = { id: 456 };
      const nombre: ICatalogoItem = { id: 13218 };
      machinery.nombre = nombre;

      activatedRoute.data = of({ machinery });
      comp.ngOnInit();

      expect(comp.catalogoItemsSharedCollection).toContain(nombre);
      expect(comp.machinery).toEqual(machinery);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMachinery>>();
      const machinery = { id: 123 };
      jest.spyOn(machineryFormService, 'getMachinery').mockReturnValue(machinery);
      jest.spyOn(machineryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ machinery });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: machinery }));
      saveSubject.complete();

      // THEN
      expect(machineryFormService.getMachinery).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(machineryService.update).toHaveBeenCalledWith(expect.objectContaining(machinery));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMachinery>>();
      const machinery = { id: 123 };
      jest.spyOn(machineryFormService, 'getMachinery').mockReturnValue({ id: null });
      jest.spyOn(machineryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ machinery: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: machinery }));
      saveSubject.complete();

      // THEN
      expect(machineryFormService.getMachinery).toHaveBeenCalled();
      expect(machineryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMachinery>>();
      const machinery = { id: 123 };
      jest.spyOn(machineryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ machinery });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(machineryService.update).toHaveBeenCalled();
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
  });
});
