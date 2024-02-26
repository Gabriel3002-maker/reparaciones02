import { IMaterialDanio, NewMaterialDanio } from './material-danio.model';

export const sampleWithRequiredData: IMaterialDanio = {
  id: 28835,
};

export const sampleWithPartialData: IMaterialDanio = {
  id: 1554,
  cantidadPedida: 25267,
};

export const sampleWithFullData: IMaterialDanio = {
  id: 18867,
  codigo: 'generally context',
  cantidadPedida: 27184,
  observacion: 'um cape',
};

export const sampleWithNewData: NewMaterialDanio = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
