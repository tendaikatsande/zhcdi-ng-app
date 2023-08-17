import dayjs from 'dayjs/esm';
import { IProvince } from 'app/entities/province/province.model';

export interface ICso {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  organisation?: string | null;
  cell?: string | null;
  city?: string | null;
  email?: string | null;
  registrationCertificate?: boolean | null;
  organisationProfile?: boolean | null;
  managementStructure?: boolean | null;
  strategicPlan?: boolean | null;
  resourceMobilisationPlan?: boolean | null;
  comments?: string | null;
  enquiries?: string | null;
  lat?: number | null;
  lng?: number | null;
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  provinces?: Pick<IProvince, 'id'>[] | null;
}

export type NewCso = Omit<ICso, 'id'> & { id: null };
