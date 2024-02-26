import dayjs from 'dayjs/esm';
import { ICatalogoItem } from 'app/entities/catalogo-item/catalogo-item.model';
import { IMaterialDanio } from 'app/entities/material-danio/material-danio.model';
import { IMaterialReporteControl } from 'app/entities/material-reporte-control/material-reporte-control.model';

export interface IMaterial {
  id: number;
  codigo?: string | null;
  nombre?: string | null;
  valorUnitario?: number | null;
  stock?: number | null;
  activo?: boolean | null;
  descripcion?: string | null;
  creadoPor?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  actualizadoPor?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
  categoria?: ICatalogoItem | null;
  materialDanio?: IMaterialDanio | null;
  materialReporteControl?: IMaterialReporteControl | null;
}

export type NewMaterial = Omit<IMaterial, 'id'> & { id: null };
