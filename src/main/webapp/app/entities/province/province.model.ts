import dayjs from 'dayjs/esm';
import { ICso } from 'app/entities/cso/cso.model';

export interface IProvince {
  id: number;
  name?: string | null;
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  csos?: Pick<ICso, 'id'>[] | null;
}

export type NewProvince = Omit<IProvince, 'id'> & { id: null };
