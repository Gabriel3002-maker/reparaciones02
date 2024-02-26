import dayjs from 'dayjs/esm';

import { IMaterial, NewMaterial } from './material.model';

export const sampleWithRequiredData: IMaterial = {
  id: 32714,
  nombre: 'caffeine intention',
  valorUnitario: 20209.13,
};

export const sampleWithPartialData: IMaterial = {
  id: 12648,
  codigo: 'yogurt',
  nombre: 'meanwhile',
  valorUnitario: 2613.11,
  stock: 10395,
  actualizadoPor: 'where relish',
};

export const sampleWithFullData: IMaterial = {
  id: 20097,
  codigo: 'parallel publicise',
  nombre: 'inquisitively',
  valorUnitario: 1608.56,
  stock: 13604,
  activo: false,
  descripcion: 'delectable plover what',
  creadoPor: 'importune blindly',
  fechaCreacion: dayjs('2024-02-26'),
  actualizadoPor: 'catacomb',
  fechaModificacion: dayjs('2024-02-26'),
};

export const sampleWithNewData: NewMaterial = {
  nombre: 'er yuppify',
  valorUnitario: 193.66,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
