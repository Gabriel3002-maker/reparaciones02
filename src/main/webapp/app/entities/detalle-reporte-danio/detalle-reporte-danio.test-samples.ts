import dayjs from 'dayjs/esm';

import { IDetalleReporteDanio, NewDetalleReporteDanio } from './detalle-reporte-danio.model';

export const sampleWithRequiredData: IDetalleReporteDanio = {
  id: 25944,
};

export const sampleWithPartialData: IDetalleReporteDanio = {
  id: 29202,
  fecha: dayjs('2024-02-26'),
  contribuyente: 'whose coinsurance',
  direccion: 'agitate packetize mistake',
  referencia: 'dishonest less',
  horasTrabajadas: 5914,
};

export const sampleWithFullData: IDetalleReporteDanio = {
  id: 8519,
  codigo: 378,
  fecha: dayjs('2024-02-26'),
  contribuyente: 'verve till',
  direccion: 'gloss',
  referencia: 'octagon',
  horasTrabajadas: 26281,
  personalResponsable: 'beside notwithstanding',
};

export const sampleWithNewData: NewDetalleReporteDanio = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
