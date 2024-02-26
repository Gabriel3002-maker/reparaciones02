import { ICatalogoItem } from 'app/entities/catalogo-item/catalogo-item.model';

export interface ICatalogo {
  id: number;
  nombre?: string | null;
  codigo?: string | null;
  descripcion?: string | null;
  items?: ICatalogoItem[] | null;
}

export type NewCatalogo = Omit<ICatalogo, 'id'> & { id: null };
