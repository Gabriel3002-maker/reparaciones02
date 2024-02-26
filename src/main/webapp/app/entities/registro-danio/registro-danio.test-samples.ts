import dayjs from 'dayjs/esm';

import { IRegistroDanio, NewRegistroDanio } from './registro-danio.model';

export const sampleWithRequiredData: IRegistroDanio = {
  id: 18730,
  codigo: 'mortise immediately inasmuch',
};

export const sampleWithPartialData: IRegistroDanio = {
  id: 13276,
  codigo: 'skywalk judgementally brisket',
  direccion: 'rapidly worrisome jacket',
};

export const sampleWithFullData: IRegistroDanio = {
  id: 29039,
  codigo: 'whose given portion',
  fecha: dayjs('2024-02-26'),
  fechaInicio: dayjs('2024-02-26'),
  fechaFin: dayjs('2024-02-25'),
  direccion: 'endpoint cruelly',
  parroquia: 'qualification unless',
  barrio: 'ha if',
};

export const sampleWithNewData: NewRegistroDanio = {
  codigo: 'seriously that',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
