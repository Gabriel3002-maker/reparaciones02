import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICatalogo } from 'app/entities/catalogo/catalogo.model';
import { CatalogoService } from 'app/entities/catalogo/service/catalogo.service';
import { CatalogoItemService } from '../service/catalogo-item.service';
import { ICatalogoItem } from '../catalogo-item.model';
import { CatalogoItemFormService } from './catalogo-item-form.service';

import { CatalogoItemUpdateComponent } from './catalogo-item-update.component';

describe('CatalogoItem Management Update Component', () => {
  let comp: CatalogoItemUpdateComponent;
  let fixture: ComponentFixture<CatalogoItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let catalogoItemFormService: CatalogoItemFormService;
  let catalogoItemService: CatalogoItemService;
  let catalogoService: CatalogoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CatalogoItemUpdateComponent],
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
      .overrideTemplate(CatalogoItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CatalogoItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    catalogoItemFormService = TestBed.inject(CatalogoItemFormService);
    catalogoItemService = TestBed.inject(CatalogoItemService);
    catalogoService = TestBed.inject(CatalogoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Catalogo query and add missing value', () => {
      const catalogoItem: ICatalogoItem = { id: 456 };
      const catalogo: ICatalogo = { id: 32083 };
      catalogoItem.catalogo = catalogo;

      const catalogoCollection: ICatalogo[] = [{ id: 23840 }];
      jest.spyOn(catalogoService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogoCollection })));
      const additionalCatalogos = [catalogo];
      const expectedCollection: ICatalogo[] = [...additionalCatalogos, ...catalogoCollection];
      jest.spyOn(catalogoService, 'addCatalogoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ catalogoItem });
      comp.ngOnInit();

      expect(catalogoService.query).toHaveBeenCalled();
      expect(catalogoService.addCatalogoToCollectionIfMissing).toHaveBeenCalledWith(
        catalogoCollection,
        ...additionalCatalogos.map(expect.objectContaining),
      );
      expect(comp.catalogosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const catalogoItem: ICatalogoItem = { id: 456 };
      const catalogo: ICatalogo = { id: 26263 };
      catalogoItem.catalogo = catalogo;

      activatedRoute.data = of({ catalogoItem });
      comp.ngOnInit();

      expect(comp.catalogosSharedCollection).toContain(catalogo);
      expect(comp.catalogoItem).toEqual(catalogoItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICatalogoItem>>();
      const catalogoItem = { id: 123 };
      jest.spyOn(catalogoItemFormService, 'getCatalogoItem').mockReturnValue(catalogoItem);
      jest.spyOn(catalogoItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogoItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: catalogoItem }));
      saveSubject.complete();

      // THEN
      expect(catalogoItemFormService.getCatalogoItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(catalogoItemService.update).toHaveBeenCalledWith(expect.objectContaining(catalogoItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICatalogoItem>>();
      const catalogoItem = { id: 123 };
      jest.spyOn(catalogoItemFormService, 'getCatalogoItem').mockReturnValue({ id: null });
      jest.spyOn(catalogoItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogoItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: catalogoItem }));
      saveSubject.complete();

      // THEN
      expect(catalogoItemFormService.getCatalogoItem).toHaveBeenCalled();
      expect(catalogoItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICatalogoItem>>();
      const catalogoItem = { id: 123 };
      jest.spyOn(catalogoItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogoItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(catalogoItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCatalogo', () => {
      it('Should forward to catalogoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(catalogoService, 'compareCatalogo');
        comp.compareCatalogo(entity, entity2);
        expect(catalogoService.compareCatalogo).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
