export interface IParametroSistema {
  id: number;
  nombre?: string | null;
  codigo?: string | null;
  clase?: string | null;
  valor?: string | null;
}

export type NewParametroSistema = Omit<IParametroSistema, 'id'> & { id: null };
