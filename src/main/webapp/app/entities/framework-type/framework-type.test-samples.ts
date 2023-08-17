import dayjs from 'dayjs/esm';

import { IFrameworkType, NewFrameworkType } from './framework-type.model';

export const sampleWithRequiredData: IFrameworkType = {
  id: 12265,
  name: 'Saint Convertible',
};

export const sampleWithPartialData: IFrameworkType = {
  id: 7686,
  name: 'Gender plunder',
};

export const sampleWithFullData: IFrameworkType = {
  id: 11757,
  name: 'Recycled',
  createdDate: dayjs('2023-08-16T18:54'),
  modifiedDate: dayjs('2023-08-16T08:52'),
};

export const sampleWithNewData: NewFrameworkType = {
  name: 'Cerium',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
