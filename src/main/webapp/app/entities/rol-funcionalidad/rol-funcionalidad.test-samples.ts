import { IRolFuncionalidad, NewRolFuncionalidad } from './rol-funcionalidad.model';

export const sampleWithRequiredData: IRolFuncionalidad = {
  id: 4361,
  rol: 'around unusual adulterate',
  prioridad: 7046,
};

export const sampleWithPartialData: IRolFuncionalidad = {
  id: 6546,
  rol: 'why',
  prioridad: 3883,
};

export const sampleWithFullData: IRolFuncionalidad = {
  id: 19343,
  rol: 'tout',
  activo: false,
  prioridad: 11716,
};

export const sampleWithNewData: NewRolFuncionalidad = {
  rol: 'defiantly supervisor against',
  prioridad: 23450,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
