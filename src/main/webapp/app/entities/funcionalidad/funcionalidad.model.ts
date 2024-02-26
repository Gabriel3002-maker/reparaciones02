export interface IFuncionalidad {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  url?: string | null;
  activo?: boolean | null;
  icono?: string | null;
  visible?: boolean | null;
  hijos?: IFuncionalidad[] | null;
  padre?: IFuncionalidad | null;
}

export type NewFuncionalidad = Omit<IFuncionalidad, 'id'> & { id: null };
