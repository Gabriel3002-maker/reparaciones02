import dayjs from 'dayjs/esm';
import { IDetalleDanio } from 'app/entities/detalle-danio/detalle-danio.model';

export interface IRegistroDanio {
  id: number;
  codigo?: string | null;
  fecha?: dayjs.Dayjs | null;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  direccion?: string | null;
  parroquia?: string | null;
  barrio?: string | null;
  detalleDanio?: IDetalleDanio | null;
}

export type NewRegistroDanio = Omit<IRegistroDanio, 'id'> & { id: null };
