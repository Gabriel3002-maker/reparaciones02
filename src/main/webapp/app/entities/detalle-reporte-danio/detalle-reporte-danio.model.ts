import dayjs from 'dayjs/esm';
import { IMaterialReporteControl } from 'app/entities/material-reporte-control/material-reporte-control.model';

export interface IDetalleReporteDanio {
  id: number;
  codigo?: number | null;
  fecha?: dayjs.Dayjs | null;
  contribuyente?: string | null;
  direccion?: string | null;
  referencia?: string | null;
  horasTrabajadas?: number | null;
  personalResponsable?: string | null;
  materialReporte?: IMaterialReporteControl | null;
}

export type NewDetalleReporteDanio = Omit<IDetalleReporteDanio, 'id'> & { id: null };
