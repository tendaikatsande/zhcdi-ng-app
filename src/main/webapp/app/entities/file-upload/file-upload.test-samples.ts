import dayjs from 'dayjs/esm';

import { IFileUpload, NewFileUpload } from './file-upload.model';

export const sampleWithRequiredData: IFileUpload = {
  id: 6029,
  filename: 'Non West Rustic',
  originalFilename: 'standardization',
  extension: 'registry hack',
  sizeInBytes: 4322,
  sha256: 'Engineer reinvent',
  contentType: 'neque',
  uploadDate: dayjs('2023-08-16T08:30'),
};

export const sampleWithPartialData: IFileUpload = {
  id: 32455,
  filename: 'whether Engineer',
  originalFilename: 'pfft apud',
  extension: 'Cambridgeshire',
  sizeInBytes: 31242,
  sha256: 'Factors Architect',
  contentType: 'pot Union',
  uploadDate: dayjs('2023-08-16T18:11'),
};

export const sampleWithFullData: IFileUpload = {
  id: 1110,
  filename: 'database Southwest',
  originalFilename: 'Rover transmit Rubber',
  extension: 'South Northeast Chevrolet',
  sizeInBytes: 20309,
  sha256: 'Berkshire Wagon Strategist',
  contentType: 'West',
  uploadDate: dayjs('2023-08-16T11:35'),
};

export const sampleWithNewData: NewFileUpload = {
  filename: 'Reggae',
  originalFilename: 'Fantastic',
  extension: 'weakly Gasoline indolent',
  sizeInBytes: 13084,
  sha256: 'backing',
  contentType: 'Central verge Bicycle',
  uploadDate: dayjs('2023-08-16T13:09'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
