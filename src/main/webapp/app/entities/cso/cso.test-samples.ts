import dayjs from 'dayjs/esm';

import { ICso, NewCso } from './cso.model';

export const sampleWithRequiredData: ICso = {
  id: 12581,
  firstName: 'Maynard',
  lastName: 'Haley',
  organisation: 'Metal',
  cell: 'especially',
  city: 'East Kathlynstead',
  email: 'Ronaldo.Wunsch@hotmail.com',
  lat: 9962,
  lng: 18163,
};

export const sampleWithPartialData: ICso = {
  id: 24133,
  firstName: 'Sarai',
  lastName: 'Cummerata',
  organisation: 'Lead Latin',
  cell: 'Communications',
  city: 'Spokane Valley',
  email: 'Everette_Rodriguez36@yahoo.com',
  registrationCertificate: true,
  comments: 'Diesel North',
  enquiries: 'searchingly Indiana Architect',
  lat: 6756,
  lng: 15985,
  createdDate: dayjs('2023-08-16T23:25'),
};

export const sampleWithFullData: ICso = {
  id: 4108,
  firstName: 'Reuben',
  lastName: 'Sipes',
  organisation: 'unhealthy transmitting Southwest',
  cell: 'Iowa',
  city: 'Schimmelland',
  email: 'Maximus_Kreiger62@gmail.com',
  registrationCertificate: true,
  organisationProfile: false,
  managementStructure: false,
  strategicPlan: false,
  resourceMobilisationPlan: false,
  comments: 'Intelligent male repurpose',
  enquiries: 'Product Pound adipisci',
  lat: 18912,
  lng: 10616,
  createdDate: dayjs('2023-08-16T07:46'),
  modifiedDate: dayjs('2023-08-16T03:14'),
};

export const sampleWithNewData: NewCso = {
  firstName: 'Linda',
  lastName: 'Rogahn',
  organisation: 'composite Cyclocross',
  cell: 'Mazda',
  city: 'Fort Tianna',
  email: 'Daniella_Sporer63@gmail.com',
  lat: 30091,
  lng: 24034,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
