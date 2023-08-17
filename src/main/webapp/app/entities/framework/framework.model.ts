import dayjs from 'dayjs/esm';
import { IFrameworkType } from 'app/entities/framework-type/framework-type.model';
import { IFileUpload } from 'app/entities/file-upload/file-upload.model';

export interface IFramework {
  id: number;
  name?: string | null;
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  frameworkTypes?: Pick<IFrameworkType, 'id'>[] | null;
  fileUploads?: Pick<IFileUpload, 'id'>[] | null;
}

export type NewFramework = Omit<IFramework, 'id'> & { id: null };
