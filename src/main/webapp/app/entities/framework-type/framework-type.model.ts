import dayjs from 'dayjs/esm';
import { IFramework } from 'app/entities/framework/framework.model';

export interface IFrameworkType {
  id: number;
  name?: string | null;
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  frameworks?: Pick<IFramework, 'id'>[] | null;
}

export type NewFrameworkType = Omit<IFrameworkType, 'id'> & { id: null };
