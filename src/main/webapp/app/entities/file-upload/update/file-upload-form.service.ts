import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFileUpload, NewFileUpload } from '../file-upload.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFileUpload for edit and NewFileUploadFormGroupInput for create.
 */
type FileUploadFormGroupInput = IFileUpload | PartialWithRequiredKeyOf<NewFileUpload>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFileUpload | NewFileUpload> = Omit<T, 'uploadDate'> & {
  uploadDate?: string | null;
};

type FileUploadFormRawValue = FormValueOf<IFileUpload>;

type NewFileUploadFormRawValue = FormValueOf<NewFileUpload>;

type FileUploadFormDefaults = Pick<NewFileUpload, 'id' | 'uploadDate' | 'frameworks'>;

type FileUploadFormGroupContent = {
  id: FormControl<FileUploadFormRawValue['id'] | NewFileUpload['id']>;
  filename: FormControl<FileUploadFormRawValue['filename']>;
  originalFilename: FormControl<FileUploadFormRawValue['originalFilename']>;
  extension: FormControl<FileUploadFormRawValue['extension']>;
  sizeInBytes: FormControl<FileUploadFormRawValue['sizeInBytes']>;
  sha256: FormControl<FileUploadFormRawValue['sha256']>;
  contentType: FormControl<FileUploadFormRawValue['contentType']>;
  uploadDate: FormControl<FileUploadFormRawValue['uploadDate']>;
  frameworks: FormControl<FileUploadFormRawValue['frameworks']>;
};

export type FileUploadFormGroup = FormGroup<FileUploadFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FileUploadFormService {
  createFileUploadFormGroup(fileUpload: FileUploadFormGroupInput = { id: null }): FileUploadFormGroup {
    const fileUploadRawValue = this.convertFileUploadToFileUploadRawValue({
      ...this.getFormDefaults(),
      ...fileUpload,
    });
    return new FormGroup<FileUploadFormGroupContent>({
      id: new FormControl(
        { value: fileUploadRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      filename: new FormControl(fileUploadRawValue.filename, {
        validators: [Validators.required],
      }),
      originalFilename: new FormControl(fileUploadRawValue.originalFilename, {
        validators: [Validators.required],
      }),
      extension: new FormControl(fileUploadRawValue.extension, {
        validators: [Validators.required],
      }),
      sizeInBytes: new FormControl(fileUploadRawValue.sizeInBytes, {
        validators: [Validators.required],
      }),
      sha256: new FormControl(fileUploadRawValue.sha256, {
        validators: [Validators.required],
      }),
      contentType: new FormControl(fileUploadRawValue.contentType, {
        validators: [Validators.required],
      }),
      uploadDate: new FormControl(fileUploadRawValue.uploadDate, {
        validators: [Validators.required],
      }),
      frameworks: new FormControl(fileUploadRawValue.frameworks ?? []),
    });
  }

  getFileUpload(form: FileUploadFormGroup): IFileUpload | NewFileUpload {
    return this.convertFileUploadRawValueToFileUpload(form.getRawValue() as FileUploadFormRawValue | NewFileUploadFormRawValue);
  }

  resetForm(form: FileUploadFormGroup, fileUpload: FileUploadFormGroupInput): void {
    const fileUploadRawValue = this.convertFileUploadToFileUploadRawValue({ ...this.getFormDefaults(), ...fileUpload });
    form.reset(
      {
        ...fileUploadRawValue,
        id: { value: fileUploadRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FileUploadFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      uploadDate: currentTime,
      frameworks: [],
    };
  }

  private convertFileUploadRawValueToFileUpload(
    rawFileUpload: FileUploadFormRawValue | NewFileUploadFormRawValue
  ): IFileUpload | NewFileUpload {
    return {
      ...rawFileUpload,
      uploadDate: dayjs(rawFileUpload.uploadDate, DATE_TIME_FORMAT),
    };
  }

  private convertFileUploadToFileUploadRawValue(
    fileUpload: IFileUpload | (Partial<NewFileUpload> & FileUploadFormDefaults)
  ): FileUploadFormRawValue | PartialWithRequiredKeyOf<NewFileUploadFormRawValue> {
    return {
      ...fileUpload,
      uploadDate: fileUpload.uploadDate ? fileUpload.uploadDate.format(DATE_TIME_FORMAT) : undefined,
      frameworks: fileUpload.frameworks ?? [],
    };
  }
}
