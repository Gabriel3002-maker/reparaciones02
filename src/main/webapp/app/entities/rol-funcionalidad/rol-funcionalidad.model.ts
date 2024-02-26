import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';

export interface IRolFuncionalidad {
  id: number;
  rol?: string | null;
  activo?: boolean | null;
  prioridad?: number | null;
  funcionalidad?: IFuncionalidad | null;
}

export type NewRolFuncionalidad = Omit<IRolFuncionalidad, 'id'> & { id: null };
