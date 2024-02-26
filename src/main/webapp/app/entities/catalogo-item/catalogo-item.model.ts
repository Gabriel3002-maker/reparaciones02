import { ICatalogo } from 'app/entities/catalogo/catalogo.model';

export interface ICatalogoItem {
  id: number;
  nombre?: string | null;
  codigo?: string | null;
  descripcion?: string | null;
  catalogoCodigo?: string | null;
  activo?: boolean | null;
  catalogo?: ICatalogo | null;
}

export type NewCatalogoItem = Omit<ICatalogoItem, 'id'> & { id: null };
