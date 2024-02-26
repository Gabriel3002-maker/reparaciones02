import { IDetalleDanio, NewDetalleDanio } from './detalle-danio.model';

export const sampleWithRequiredData: IDetalleDanio = {
  id: 158,
  codigo: 'overhear yowza secret',
};

export const sampleWithPartialData: IDetalleDanio = {
  id: 3618,
  codigo: 'jovially whereas indeed',
  tecnico: 'lest boohoo',
  namePerson: 'lamp',
  direccion: 'invite parrot',
  observacion: 'duh oof contemplate',
};

export const sampleWithFullData: IDetalleDanio = {
  id: 31537,
  codigo: 'emission',
  descripcionDanio: 'ick past',
  tecnico: 'livid voluminous stalk',
  namePerson: 'rationalize hmph till',
  direccion: 'ply sinful oh',
  estadoReparacion: 'youthfully',
  observacion: 'ack',
};

export const sampleWithNewData: NewDetalleDanio = {
  codigo: 'truthfully shirk piercing',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
