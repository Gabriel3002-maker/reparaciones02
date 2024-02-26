import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

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
import { DetalleDanioService } from '../service/detalle-danio.service';
import { IDetalleDanio } from '../detalle-danio.model';
import { DetalleDanioFormService, DetalleDanioFormGroup } from './detalle-danio-form.service';

@Component({
  standalone: true,
  selector: 'jhi-detalle-danio-update',
  templateUrl: './detalle-danio-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DetalleDanioUpdateComponent implements OnInit {
  isSaving = false;
  detalleDanio: IDetalleDanio | null = null;

  catalogoItemsSharedCollection: ICatalogoItem[] = [];
  materialDaniosSharedCollection: IMaterialDanio[] = [];
  machinerySharedCollection: IMachinery[] = [];
  ordenBodegasSharedCollection: IOrdenBodega[] = [];
  detalleReporteDaniosSharedCollection: IDetalleReporteDanio[] = [];

  editForm: DetalleDanioFormGroup = this.detalleDanioFormService.createDetalleDanioFormGroup();

  constructor(
    protected detalleDanioService: DetalleDanioService,
    protected detalleDanioFormService: DetalleDanioFormService,
    protected catalogoItemService: CatalogoItemService,
    protected materialDanioService: MaterialDanioService,
    protected machineryService: MachineryService,
    protected ordenBodegaService: OrdenBodegaService,
    protected detalleReporteDanioService: DetalleReporteDanioService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCatalogoItem = (o1: ICatalogoItem | null, o2: ICatalogoItem | null): boolean =>
    this.catalogoItemService.compareCatalogoItem(o1, o2);

  compareMaterialDanio = (o1: IMaterialDanio | null, o2: IMaterialDanio | null): boolean =>
    this.materialDanioService.compareMaterialDanio(o1, o2);

  compareMachinery = (o1: IMachinery | null, o2: IMachinery | null): boolean => this.machineryService.compareMachinery(o1, o2);

  compareOrdenBodega = (o1: IOrdenBodega | null, o2: IOrdenBodega | null): boolean => this.ordenBodegaService.compareOrdenBodega(o1, o2);

  compareDetalleReporteDanio = (o1: IDetalleReporteDanio | null, o2: IDetalleReporteDanio | null): boolean =>
    this.detalleReporteDanioService.compareDetalleReporteDanio(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detalleDanio }) => {
      this.detalleDanio = detalleDanio;
      if (detalleDanio) {
        this.updateForm(detalleDanio);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const detalleDanio = this.detalleDanioFormService.getDetalleDanio(this.editForm);
    if (detalleDanio.id !== null) {
      this.subscribeToSaveResponse(this.detalleDanioService.update(detalleDanio));
    } else {
      this.subscribeToSaveResponse(this.detalleDanioService.create(detalleDanio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDetalleDanio>>): void {
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

  protected updateForm(detalleDanio: IDetalleDanio): void {
    this.detalleDanio = detalleDanio;
    this.detalleDanioFormService.resetForm(this.editForm, detalleDanio);

    this.catalogoItemsSharedCollection = this.catalogoItemService.addCatalogoItemToCollectionIfMissing<ICatalogoItem>(
      this.catalogoItemsSharedCollection,
      detalleDanio.tipoDanio,
    );
    this.materialDaniosSharedCollection = this.materialDanioService.addMaterialDanioToCollectionIfMissing<IMaterialDanio>(
      this.materialDaniosSharedCollection,
      detalleDanio.materialDanio,
    );
    this.machinerySharedCollection = this.machineryService.addMachineryToCollectionIfMissing<IMachinery>(
      this.machinerySharedCollection,
      detalleDanio.maquinaria,
    );
    this.ordenBodegasSharedCollection = this.ordenBodegaService.addOrdenBodegaToCollectionIfMissing<IOrdenBodega>(
      this.ordenBodegasSharedCollection,
      detalleDanio.ordenBodega,
    );
    this.detalleReporteDaniosSharedCollection =
      this.detalleReporteDanioService.addDetalleReporteDanioToCollectionIfMissing<IDetalleReporteDanio>(
        this.detalleReporteDaniosSharedCollection,
        detalleDanio.detalleReporteDanio,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.catalogoItemService
      .query()
      .pipe(map((res: HttpResponse<ICatalogoItem[]>) => res.body ?? []))
      .pipe(
        map((catalogoItems: ICatalogoItem[]) =>
          this.catalogoItemService.addCatalogoItemToCollectionIfMissing<ICatalogoItem>(catalogoItems, this.detalleDanio?.tipoDanio),
        ),
      )
      .subscribe((catalogoItems: ICatalogoItem[]) => (this.catalogoItemsSharedCollection = catalogoItems));

    this.materialDanioService
      .query()
      .pipe(map((res: HttpResponse<IMaterialDanio[]>) => res.body ?? []))
      .pipe(
        map((materialDanios: IMaterialDanio[]) =>
          this.materialDanioService.addMaterialDanioToCollectionIfMissing<IMaterialDanio>(materialDanios, this.detalleDanio?.materialDanio),
        ),
      )
      .subscribe((materialDanios: IMaterialDanio[]) => (this.materialDaniosSharedCollection = materialDanios));

    this.machineryService
      .query()
      .pipe(map((res: HttpResponse<IMachinery[]>) => res.body ?? []))
      .pipe(
        map((machinery: IMachinery[]) =>
          this.machineryService.addMachineryToCollectionIfMissing<IMachinery>(machinery, this.detalleDanio?.maquinaria),
        ),
      )
      .subscribe((machinery: IMachinery[]) => (this.machinerySharedCollection = machinery));

    this.ordenBodegaService
      .query()
      .pipe(map((res: HttpResponse<IOrdenBodega[]>) => res.body ?? []))
      .pipe(
        map((ordenBodegas: IOrdenBodega[]) =>
          this.ordenBodegaService.addOrdenBodegaToCollectionIfMissing<IOrdenBodega>(ordenBodegas, this.detalleDanio?.ordenBodega),
        ),
      )
      .subscribe((ordenBodegas: IOrdenBodega[]) => (this.ordenBodegasSharedCollection = ordenBodegas));

    this.detalleReporteDanioService
      .query()
      .pipe(map((res: HttpResponse<IDetalleReporteDanio[]>) => res.body ?? []))
      .pipe(
        map((detalleReporteDanios: IDetalleReporteDanio[]) =>
          this.detalleReporteDanioService.addDetalleReporteDanioToCollectionIfMissing<IDetalleReporteDanio>(
            detalleReporteDanios,
            this.detalleDanio?.detalleReporteDanio,
          ),
        ),
      )
      .subscribe((detalleReporteDanios: IDetalleReporteDanio[]) => (this.detalleReporteDaniosSharedCollection = detalleReporteDanios));
  }
}
