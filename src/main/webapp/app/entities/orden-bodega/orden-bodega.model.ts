import dayjs from 'dayjs/esm';
import { IPersona } from 'app/entities/persona/persona.model';

export interface IOrdenBodega {
  id: number;
  codigo?: string | null;
  detalleNecesidad?: string | null;
  fecha?: dayjs.Dayjs | null;
  receptor?: IPersona | null;
}

export type NewOrdenBodega = Omit<IOrdenBodega, 'id'> & { id: null };
