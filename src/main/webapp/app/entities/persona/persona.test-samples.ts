import { IPersona, NewPersona } from './persona.model';

export const sampleWithRequiredData: IPersona = {
  id: 30651,
  identificacion: 'traveler biodegradable',
  primerApellido: 'than dent orderly',
  primerNombre: 'to diet',
};

export const sampleWithPartialData: IPersona = {
  id: 21414,
  identificacion: 'jumpy revile',
  primerApellido: 'zowie',
  primerNombre: 'bustling crow',
  correo: 'vainly ew',
};

export const sampleWithFullData: IPersona = {
  id: 14388,
  identificacion: 'renaissance rigidly',
  primerApellido: 'but annually bashfully',
  segundoApellido: 'goal helplessly definitive',
  primerNombre: 'exemplary yowza',
  segundoNombre: 'ha yuck following',
  celular: 'butter steam',
  telefonoConvencional: 'gadzooks ha uphold',
  correo: 'near',
};

export const sampleWithNewData: NewPersona = {
  identificacion: 'whose',
  primerApellido: 'choose poorly yippee',
  primerNombre: 'inasmuch pointed shameless',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
