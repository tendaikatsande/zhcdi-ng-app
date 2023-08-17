import dayjs from 'dayjs/esm';

import { IProvince, NewProvince } from './province.model';

export const sampleWithRequiredData: IProvince = {
  id: 187,
  name: 'Gloves utilisation',
};

export const sampleWithPartialData: IProvince = {
  id: 18690,
  name: 'Direct Antarctica',
};

export const sampleWithFullData: IProvince = {
  id: 9298,
  name: 'invoice',
  createdDate: dayjs('2023-08-16T12:48'),
  modifiedDate: dayjs('2023-08-16T20:19'),
};

export const sampleWithNewData: NewProvince = {
  name: 'silver',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
