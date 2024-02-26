import { IMaterial } from 'app/entities/material/material.model';

export interface IMaterialDanio {
  id: number;
  codigo?: string | null;
  cantidadPedida?: number | null;
  observacion?: string | null;
  materiales?: IMaterial[] | null;
}

export type NewMaterialDanio = Omit<IMaterialDanio, 'id'> & { id: null };
