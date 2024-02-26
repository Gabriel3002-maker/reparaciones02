import { IFuncionalidad, NewFuncionalidad } from './funcionalidad.model';

export const sampleWithRequiredData: IFuncionalidad = {
  id: 21362,
  nombre: 'whereas disperse whether',
  activo: false,
};

export const sampleWithPartialData: IFuncionalidad = {
  id: 15801,
  nombre: 'daily searchingly aromatic',
  url: 'https://poised-wake.com.es',
  activo: true,
  icono: 'especially all',
  visible: true,
};

export const sampleWithFullData: IFuncionalidad = {
  id: 11632,
  nombre: 'dueling whose apron',
  descripcion: 'or fake upright',
  url: 'https://tart-wholesaler.org',
  activo: false,
  icono: 'instead',
  visible: false,
};

export const sampleWithNewData: NewFuncionalidad = {
  nombre: 'where chart',
  activo: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
