import dayjs from 'dayjs/esm';

import { IOrdenBodega, NewOrdenBodega } from './orden-bodega.model';

export const sampleWithRequiredData: IOrdenBodega = {
  id: 6465,
};

export const sampleWithPartialData: IOrdenBodega = {
  id: 11021,
  codigo: 'briefly while',
  fecha: dayjs('2024-02-25'),
};

export const sampleWithFullData: IOrdenBodega = {
  id: 4739,
  codigo: 'duh instead',
  detalleNecesidad: 'folder immediately',
  fecha: dayjs('2024-02-26'),
};

export const sampleWithNewData: NewOrdenBodega = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
