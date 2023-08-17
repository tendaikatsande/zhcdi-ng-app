import dayjs from 'dayjs/esm';
import { IFramework } from 'app/entities/framework/framework.model';

export interface IFileUpload {
  id: number;
  filename?: string | null;
  originalFilename?: string | null;
  extension?: string | null;
  sizeInBytes?: number | null;
  sha256?: string | null;
  contentType?: string | null;
  uploadDate?: dayjs.Dayjs | null;
  frameworks?: Pick<IFramework, 'id'>[] | null;
}

export type NewFileUpload = Omit<IFileUpload, 'id'> & { id: null };
