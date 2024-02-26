import { ICatalogoItem } from 'app/entities/catalogo-item/catalogo-item.model';
import { IUser } from 'app/entities/user/user.model';

export interface IPersona {
  id: number;
  identificacion?: string | null;
  primerApellido?: string | null;
  segundoApellido?: string | null;
  primerNombre?: string | null;
  segundoNombre?: string | null;
  celular?: string | null;
  telefonoConvencional?: string | null;
  correo?: string | null;
  tipoIdentificacion?: ICatalogoItem | null;
  usuario?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewPersona = Omit<IPersona, 'id'> & { id: null };
