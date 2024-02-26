import { ICatalogoItem } from 'app/entities/catalogo-item/catalogo-item.model';

export interface IMachinery {
  id: number;
  codigo?: string | null;
  descripcion?: string | null;
  horasTrabajadas?: number | null;
  nombre?: ICatalogoItem | null;
}

export type NewMachinery = Omit<IMachinery, 'id'> & { id: null };
