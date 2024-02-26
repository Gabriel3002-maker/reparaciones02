import { IMaterialReporteControl, NewMaterialReporteControl } from './material-reporte-control.model';

export const sampleWithRequiredData: IMaterialReporteControl = {
  id: 23742,
};

export const sampleWithPartialData: IMaterialReporteControl = {
  id: 27074,
  codigo: 'suspiciously',
  observacion: 'minus',
};

export const sampleWithFullData: IMaterialReporteControl = {
  id: 29665,
  codigo: 'behalf',
  cantidadUsada: 24854,
  observacion: 'bah',
};

export const sampleWithNewData: NewMaterialReporteControl = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
