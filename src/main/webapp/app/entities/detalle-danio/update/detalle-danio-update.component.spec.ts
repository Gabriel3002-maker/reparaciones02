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
import { IMachinery } from 'app/entities/machinery/machinery.model';
import { MachineryService } from 'app/entities/machinery/service/machinery.service';
import { IOrdenBodega } from 'app/entities/orden-bodega/orden-bodega.model';
import { OrdenBodegaService } from 'app/entities/orden-bodega/service/orden-bodega.service';
import { IDetalleReporteDanio } from 'app/entities/detalle-reporte-danio/detalle-reporte-danio.model';
import { DetalleReporteDanioService } from 'app/entities/detalle-reporte-danio/service/detalle-reporte-danio.service';
import { IDetalleDanio } from '../detalle-danio.model';
import { DetalleDanioService } from '../service/detalle-danio.service';
import { DetalleDanioFormService } from './detalle-danio-form.service';

import { DetalleDanioUpdateComponent } from './detalle-danio-update.component';

describe('DetalleDanio Management Update Component', () => {
  let comp: DetalleDanioUpdateComponent;
  let fixture: ComponentFixture<DetalleDanioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let detalleDanioFormService: DetalleDanioFormService;
  let detalleDanioService: DetalleDanioService;
  let catalogoItemService: CatalogoItemService;
  let materialDanioService: MaterialDanioService;
  let machineryService: MachineryService;
  let ordenBodegaService: OrdenBodegaService;
  let detalleReporteDanioService: DetalleReporteDanioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DetalleDanioUpdateComponent],
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
      .overrideTemplate(DetalleDanioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DetalleDanioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    detalleDanioFormService = TestBed.inject(DetalleDanioFormService);
    detalleDanioService = TestBed.inject(DetalleDanioService);
    catalogoItemService = TestBed.inject(CatalogoItemService);
    materialDanioService = TestBed.inject(MaterialDanioService);
    machineryService = TestBed.inject(MachineryService);
    ordenBodegaService = TestBed.inject(OrdenBodegaService);
    detalleReporteDanioService = TestBed.inject(DetalleReporteDanioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CatalogoItem query and add missing value', () => {
      const detalleDanio: IDetalleDanio = { id: 456 };
      const tipoDanio: ICatalogoItem = { id: 28622 };
      detalleDanio.tipoDanio = tipoDanio;

      const catalogoItemCollection: ICatalogoItem[] = [{ id: 16990 }];
      jest.spyOn(catalogoItemService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogoItemCollection })));
      const additionalCatalogoItems = [tipoDanio];
      const expectedCollection: ICatalogoItem[] = [...additionalCatalogoItems, ...catalogoItemCollection];
      jest.spyOn(catalogoItemService, 'addCatalogoItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detalleDanio });
      comp.ngOnInit();

      expect(catalogoItemService.query).toHaveBeenCalled();
      expect(catalogoItemService.addCatalogoItemToCollectionIfMissing).toHaveBeenCalledWith(
        catalogoItemCollection,
        ...additionalCatalogoItems.map(expect.objectContaining),
      );
      expect(comp.catalogoItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call MaterialDanio query and add missing value', () => {
      const detalleDanio: IDetalleDanio = { id: 456 };
      const materialDanio: IMaterialDanio = { id: 6542 };
      detalleDanio.materialDanio = materialDanio;

      const materialDanioCollection: IMaterialDanio[] = [{ id: 24456 }];
      jest.spyOn(materialDanioService, 'query').mockReturnValue(of(new HttpResponse({ body: materialDanioCollection })));
      const additionalMaterialDanios = [materialDanio];
      const expectedCollection: IMaterialDanio[] = [...additionalMaterialDanios, ...materialDanioCollection];
      jest.spyOn(materialDanioService, 'addMaterialDanioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detalleDanio });
      comp.ngOnInit();

      expect(materialDanioService.query).toHaveBeenCalled();
      expect(materialDanioService.addMaterialDanioToCollectionIfMissing).toHaveBeenCalledWith(
        materialDanioCollection,
        ...additionalMaterialDanios.map(expect.objectContaining),
      );
      expect(comp.materialDaniosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Machinery query and add missing value', () => {
      const detalleDanio: IDetalleDanio = { id: 456 };
      const maquinaria: IMachinery = { id: 31766 };
      detalleDanio.maquinaria = maquinaria;

      const machineryCollection: IMachinery[] = [{ id: 26480 }];
      jest.spyOn(machineryService, 'query').mockReturnValue(of(new HttpResponse({ body: machineryCollection })));
      const additionalMachinery = [maquinaria];
      const expectedCollection: IMachinery[] = [...additionalMachinery, ...machineryCollection];
      jest.spyOn(machineryService, 'addMachineryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detalleDanio });
      comp.ngOnInit();

      expect(machineryService.query).toHaveBeenCalled();
      expect(machineryService.addMachineryToCollectionIfMissing).toHaveBeenCalledWith(
        machineryCollection,
        ...additionalMachinery.map(expect.objectContaining),
      );
      expect(comp.machinerySharedCollection).toEqual(expectedCollection);
    });

    it('Should call OrdenBodega query and add missing value', () => {
      const detalleDanio: IDetalleDanio = { id: 456 };
      const ordenBodega: IOrdenBodega = { id: 28233 };
      detalleDanio.ordenBodega = ordenBodega;

      const ordenBodegaCollection: IOrdenBodega[] = [{ id: 18572 }];
      jest.spyOn(ordenBodegaService, 'query').mockReturnValue(of(new HttpResponse({ body: ordenBodegaCollection })));
      const additionalOrdenBodegas = [ordenBodega];
      const expectedCollection: IOrdenBodega[] = [...additionalOrdenBodegas, ...ordenBodegaCollection];
      jest.spyOn(ordenBodegaService, 'addOrdenBodegaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detalleDanio });
      comp.ngOnInit();

      expect(ordenBodegaService.query).toHaveBeenCalled();
      expect(ordenBodegaService.addOrdenBodegaToCollectionIfMissing).toHaveBeenCalledWith(
        ordenBodegaCollection,
        ...additionalOrdenBodegas.map(expect.objectContaining),
      );
      expect(comp.ordenBodegasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DetalleReporteDanio query and add missing value', () => {
      const detalleDanio: IDetalleDanio = { id: 456 };
      const detalleReporteDanio: IDetalleReporteDanio = { id: 23153 };
      detalleDanio.detalleReporteDanio = detalleReporteDanio;

      const detalleReporteDanioCollection: IDetalleReporteDanio[] = [{ id: 1118 }];
      jest.spyOn(detalleReporteDanioService, 'query').mockReturnValue(of(new HttpResponse({ body: detalleReporteDanioCollection })));
      const additionalDetalleReporteDanios = [detalleReporteDanio];
      const expectedCollection: IDetalleReporteDanio[] = [...additionalDetalleReporteDanios, ...detalleReporteDanioCollection];
      jest.spyOn(detalleReporteDanioService, 'addDetalleReporteDanioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detalleDanio });
      comp.ngOnInit();

      expect(detalleReporteDanioService.query).toHaveBeenCalled();
      expect(detalleReporteDanioService.addDetalleReporteDanioToCollectionIfMissing).toHaveBeenCalledWith(
        detalleReporteDanioCollection,
        ...additionalDetalleReporteDanios.map(expect.objectContaining),
      );
      expect(comp.detalleReporteDaniosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const detalleDanio: IDetalleDanio = { id: 456 };
      const tipoDanio: ICatalogoItem = { id: 30804 };
      detalleDanio.tipoDanio = tipoDanio;
      const materialDanio: IMaterialDanio = { id: 24562 };
      detalleDanio.materialDanio = materialDanio;
      const maquinaria: IMachinery = { id: 7212 };
      detalleDanio.maquinaria = maquinaria;
      const ordenBodega: IOrdenBodega = { id: 4142 };
      detalleDanio.ordenBodega = ordenBodega;
      const detalleReporteDanio: IDetalleReporteDanio = { id: 26662 };
      detalleDanio.detalleReporteDanio = detalleReporteDanio;

      activatedRoute.data = of({ detalleDanio });
      comp.ngOnInit();

      expect(comp.catalogoItemsSharedCollection).toContain(tipoDanio);
      expect(comp.materialDaniosSharedCollection).toContain(materialDanio);
      expect(comp.machinerySharedCollection).toContain(maquinaria);
      expect(comp.ordenBodegasSharedCollection).toContain(ordenBodega);
      expect(comp.detalleReporteDaniosSharedCollection).toContain(detalleReporteDanio);
      expect(comp.detalleDanio).toEqual(detalleDanio);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDetalleDanio>>();
      const detalleDanio = { id: 123 };
      jest.spyOn(detalleDanioFormService, 'getDetalleDanio').mockReturnValue(detalleDanio);
      jest.spyOn(detalleDanioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleDanio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detalleDanio }));
      saveSubject.complete();

      // THEN
      expect(detalleDanioFormService.getDetalleDanio).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(detalleDanioService.update).toHaveBeenCalledWith(expect.objectContaining(detalleDanio));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDetalleDanio>>();
      const detalleDanio = { id: 123 };
      jest.spyOn(detalleDanioFormService, 'getDetalleDanio').mockReturnValue({ id: null });
      jest.spyOn(detalleDanioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleDanio: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detalleDanio }));
      saveSubject.complete();

      // THEN
      expect(detalleDanioFormService.getDetalleDanio).toHaveBeenCalled();
      expect(detalleDanioService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDetalleDanio>>();
      const detalleDanio = { id: 123 };
      jest.spyOn(detalleDanioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleDanio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(detalleDanioService.update).toHaveBeenCalled();
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

    describe('compareMachinery', () => {
      it('Should forward to machineryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(machineryService, 'compareMachinery');
        comp.compareMachinery(entity, entity2);
        expect(machineryService.compareMachinery).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOrdenBodega', () => {
      it('Should forward to ordenBodegaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ordenBodegaService, 'compareOrdenBodega');
        comp.compareOrdenBodega(entity, entity2);
        expect(ordenBodegaService.compareOrdenBodega).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDetalleReporteDanio', () => {
      it('Should forward to detalleReporteDanioService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(detalleReporteDanioService, 'compareDetalleReporteDanio');
        comp.compareDetalleReporteDanio(entity, entity2);
        expect(detalleReporteDanioService.compareDetalleReporteDanio).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
