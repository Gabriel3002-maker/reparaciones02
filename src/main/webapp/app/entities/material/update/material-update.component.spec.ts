import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICatalogoItem } from 'app/entities/catalogo-item/catalogo-item.model';
import { CatalogoItemService } from 'app/entities/catalogo-item/service/catalogo-item.service';
import { IMaterialDanio } from 'app/entities/material-danio/material-danio.model';
import { MaterialDanioService } from 'app/entities/material-danio/service/material-danio.service';
import { IMaterialReporteControl } from 'app/entities/material-reporte-control/material-reporte-control.model';
import { MaterialReporteControlService } from 'app/entities/material-reporte-control/service/material-reporte-control.service';
import { IMaterial } from '../material.model';
import { MaterialService } from '../service/material.service';
import { MaterialFormService } from './material-form.service';

import { MaterialUpdateComponent } from './material-update.component';

describe('Material Management Update Component', () => {
  let comp: MaterialUpdateComponent;
  let fixture: ComponentFixture<MaterialUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let materialFormService: MaterialFormService;
  let materialService: MaterialService;
  let catalogoItemService: CatalogoItemService;
  let materialDanioService: MaterialDanioService;
  let materialReporteControlService: MaterialReporteControlService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MaterialUpdateComponent],
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
      .overrideTemplate(MaterialUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaterialUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    materialFormService = TestBed.inject(MaterialFormService);
    materialService = TestBed.inject(MaterialService);
    catalogoItemService = TestBed.inject(CatalogoItemService);
    materialDanioService = TestBed.inject(MaterialDanioService);
    materialReporteControlService = TestBed.inject(MaterialReporteControlService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CatalogoItem query and add missing value', () => {
      const material: IMaterial = { id: 456 };
      const categoria: ICatalogoItem = { id: 5221 };
      material.categoria = categoria;

      const catalogoItemCollection: ICatalogoItem[] = [{ id: 6777 }];
      jest.spyOn(catalogoItemService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogoItemCollection })));
      const additionalCatalogoItems = [categoria];
      const expectedCollection: ICatalogoItem[] = [...additionalCatalogoItems, ...catalogoItemCollection];
      jest.spyOn(catalogoItemService, 'addCatalogoItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ material });
      comp.ngOnInit();

      expect(catalogoItemService.query).toHaveBeenCalled();
      expect(catalogoItemService.addCatalogoItemToCollectionIfMissing).toHaveBeenCalledWith(
        catalogoItemCollection,
        ...additionalCatalogoItems.map(expect.objectContaining),
      );
      expect(comp.catalogoItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call MaterialDanio query and add missing value', () => {
      const material: IMaterial = { id: 456 };
      const materialDanio: IMaterialDanio = { id: 4314 };
      material.materialDanio = materialDanio;

      const materialDanioCollection: IMaterialDanio[] = [{ id: 20518 }];
      jest.spyOn(materialDanioService, 'query').mockReturnValue(of(new HttpResponse({ body: materialDanioCollection })));
      const additionalMaterialDanios = [materialDanio];
      const expectedCollection: IMaterialDanio[] = [...additionalMaterialDanios, ...materialDanioCollection];
      jest.spyOn(materialDanioService, 'addMaterialDanioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ material });
      comp.ngOnInit();

      expect(materialDanioService.query).toHaveBeenCalled();
      expect(materialDanioService.addMaterialDanioToCollectionIfMissing).toHaveBeenCalledWith(
        materialDanioCollection,
        ...additionalMaterialDanios.map(expect.objectContaining),
      );
      expect(comp.materialDaniosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call MaterialReporteControl query and add missing value', () => {
      const material: IMaterial = { id: 456 };
      const materialReporteControl: IMaterialReporteControl = { id: 1066 };
      material.materialReporteControl = materialReporteControl;

      const materialReporteControlCollection: IMaterialReporteControl[] = [{ id: 13317 }];
      jest.spyOn(materialReporteControlService, 'query').mockReturnValue(of(new HttpResponse({ body: materialReporteControlCollection })));
      const additionalMaterialReporteControls = [materialReporteControl];
      const expectedCollection: IMaterialReporteControl[] = [...additionalMaterialReporteControls, ...materialReporteControlCollection];
      jest.spyOn(materialReporteControlService, 'addMaterialReporteControlToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ material });
      comp.ngOnInit();

      expect(materialReporteControlService.query).toHaveBeenCalled();
      expect(materialReporteControlService.addMaterialReporteControlToCollectionIfMissing).toHaveBeenCalledWith(
        materialReporteControlCollection,
        ...additionalMaterialReporteControls.map(expect.objectContaining),
      );
      expect(comp.materialReporteControlsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const material: IMaterial = { id: 456 };
      const categoria: ICatalogoItem = { id: 3941 };
      material.categoria = categoria;
      const materialDanio: IMaterialDanio = { id: 3871 };
      material.materialDanio = materialDanio;
      const materialReporteControl: IMaterialReporteControl = { id: 12093 };
      material.materialReporteControl = materialReporteControl;

      activatedRoute.data = of({ material });
      comp.ngOnInit();

      expect(comp.catalogoItemsSharedCollection).toContain(categoria);
      expect(comp.materialDaniosSharedCollection).toContain(materialDanio);
      expect(comp.materialReporteControlsSharedCollection).toContain(materialReporteControl);
      expect(comp.material).toEqual(material);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterial>>();
      const material = { id: 123 };
      jest.spyOn(materialFormService, 'getMaterial').mockReturnValue(material);
      jest.spyOn(materialService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ material });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: material }));
      saveSubject.complete();

      // THEN
      expect(materialFormService.getMaterial).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(materialService.update).toHaveBeenCalledWith(expect.objectContaining(material));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterial>>();
      const material = { id: 123 };
      jest.spyOn(materialFormService, 'getMaterial').mockReturnValue({ id: null });
      jest.spyOn(materialService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ material: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: material }));
      saveSubject.complete();

      // THEN
      expect(materialFormService.getMaterial).toHaveBeenCalled();
      expect(materialService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterial>>();
      const material = { id: 123 };
      jest.spyOn(materialService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ material });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(materialService.update).toHaveBeenCalled();
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

    describe('compareMaterialDanio', () => {
      it('Should forward to materialDanioService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(materialDanioService, 'compareMaterialDanio');
        comp.compareMaterialDanio(entity, entity2);
        expect(materialDanioService.compareMaterialDanio).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMaterialReporteControl', () => {
      it('Should forward to materialReporteControlService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(materialReporteControlService, 'compareMaterialReporteControl');
        comp.compareMaterialReporteControl(entity, entity2);
        expect(materialReporteControlService.compareMaterialReporteControl).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
