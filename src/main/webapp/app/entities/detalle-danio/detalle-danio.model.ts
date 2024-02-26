import { ICatalogoItem } from 'app/entities/catalogo-item/catalogo-item.model';
import { IMaterialDanio } from 'app/entities/material-danio/material-danio.model';
import { IMachinery } from 'app/entities/machinery/machinery.model';
import { IOrdenBodega } from 'app/entities/orden-bodega/orden-bodega.model';
import { IDetalleReporteDanio } from 'app/entities/detalle-reporte-danio/detalle-reporte-danio.model';

export interface IDetalleDanio {
  id: number;
  codigo?: string | null;
  descripcionDanio?: string | null;
  tecnico?: string | null;
  namePerson?: string | null;
  direccion?: string | null;
  estadoReparacion?: string | null;
  observacion?: string | null;
  tipoDanio?: ICatalogoItem | null;
  materialDanio?: IMaterialDanio | null;
  maquinaria?: IMachinery | null;
  ordenBodega?: IOrdenBodega | null;
  detalleReporteDanio?: IDetalleReporteDanio | null;
}

export type NewDetalleDanio = Omit<IDetalleDanio, 'id'> & { id: null };
