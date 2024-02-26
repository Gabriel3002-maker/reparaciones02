import { IParametroSistema, NewParametroSistema } from './parametro-sistema.model';

export const sampleWithRequiredData: IParametroSistema = {
  id: 18313,
  nombre: 'bah',
  codigo: 'quirkily happily mmm',
  clase: 'blah warmhearted until',
  valor: 'suspiciously',
};

export const sampleWithPartialData: IParametroSistema = {
  id: 14507,
  nombre: 'anenst exist punctually',
  codigo: 'meanwhile situate milestone',
  clase: 'hastily',
  valor: 'encroach finance',
};

export const sampleWithFullData: IParametroSistema = {
  id: 10653,
  nombre: 'nautical',
  codigo: 'out mmm',
  clase: 'unless',
  valor: 'blank radiator',
};

export const sampleWithNewData: NewParametroSistema = {
  nombre: 'atop alongside meh',
  codigo: 'incidentally',
  clase: 'worrisome successfully',
  valor: 'overcooked aged triangular',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
