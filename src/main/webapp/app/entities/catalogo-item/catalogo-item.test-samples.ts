import { ICatalogoItem, NewCatalogoItem } from './catalogo-item.model';

export const sampleWithRequiredData: ICatalogoItem = {
  id: 10796,
  nombre: 'afterwards unto',
  codigo: 'fabric when before',
  catalogoCodigo: 'scaly adsorb bah',
};

export const sampleWithPartialData: ICatalogoItem = {
  id: 15621,
  nombre: 'mourn',
  codigo: 'growing phew wherever',
  catalogoCodigo: 'duh solemnise',
};

export const sampleWithFullData: ICatalogoItem = {
  id: 13612,
  nombre: 'ick infatuated',
  codigo: 'big metronome commercialize',
  descripcion: 'as gyrate from',
  catalogoCodigo: 'and annul',
  activo: false,
};

export const sampleWithNewData: NewCatalogoItem = {
  nombre: 'weakly',
  codigo: 'phooey',
  catalogoCodigo: 'after ouch',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
