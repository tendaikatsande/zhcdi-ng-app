import dayjs from 'dayjs/esm';

import { IFramework, NewFramework } from './framework.model';

export const sampleWithRequiredData: IFramework = {
  id: 8988,
  name: 'ouch Latin',
};

export const sampleWithPartialData: IFramework = {
  id: 31362,
  name: 'Tasty Granite',
  modifiedDate: dayjs('2023-08-16T06:33'),
};

export const sampleWithFullData: IFramework = {
  id: 2832,
  name: 'Diesel',
  createdDate: dayjs('2023-08-16T02:52'),
  modifiedDate: dayjs('2023-08-16T12:52'),
};

export const sampleWithNewData: NewFramework = {
  name: 'Tuna Southeast',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
