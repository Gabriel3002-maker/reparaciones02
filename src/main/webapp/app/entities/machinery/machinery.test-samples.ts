import { IMachinery, NewMachinery } from './machinery.model';

export const sampleWithRequiredData: IMachinery = {
  id: 6412,
};

export const sampleWithPartialData: IMachinery = {
  id: 22216,
  codigo: 'creditor swan',
  descripcion: 'sanitise enigma ringed',
  horasTrabajadas: 31405.18,
};

export const sampleWithFullData: IMachinery = {
  id: 4964,
  codigo: 'spatula overlooked',
  descripcion: 'bespeak meaningfully',
  horasTrabajadas: 24257.65,
};

export const sampleWithNewData: NewMachinery = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
