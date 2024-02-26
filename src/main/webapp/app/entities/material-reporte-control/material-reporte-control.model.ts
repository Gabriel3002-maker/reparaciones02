import { IMaterial } from 'app/entities/material/material.model';

export interface IMaterialReporteControl {
  id: number;
  codigo?: string | null;
  cantidadUsada?: number | null;
  observacion?: string | null;
  materiales?: IMaterial[] | null;
}

export type NewMaterialReporteControl = Omit<IMaterialReporteControl, 'id'> & { id: null };
