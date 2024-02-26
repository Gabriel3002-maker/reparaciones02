import { ICatalogo, NewCatalogo } from './catalogo.model';

export const sampleWithRequiredData: ICatalogo = {
  id: 2371,
  nombre: 'saucer',
  codigo: 'why beyond planula',
};

export const sampleWithPartialData: ICatalogo = {
  id: 3459,
  nombre: 'flowery true',
  codigo: 'blight',
  descripcion: 'highly whether flair',
};

export const sampleWithFullData: ICatalogo = {
  id: 19562,
  nombre: 'triumphantly vaguely',
  codigo: 'although though',
  descripcion: 'yieldingly remorseful',
};

export const sampleWithNewData: NewCatalogo = {
  nombre: 'suss',
  codigo: 'wildly staple',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
