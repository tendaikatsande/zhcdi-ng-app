import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFramework, NewFramework } from '../framework.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFramework for edit and NewFrameworkFormGroupInput for create.
 */
type FrameworkFormGroupInput = IFramework | PartialWithRequiredKeyOf<NewFramework>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFramework | NewFramework> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

type FrameworkFormRawValue = FormValueOf<IFramework>;

type NewFrameworkFormRawValue = FormValueOf<NewFramework>;

type FrameworkFormDefaults = Pick<NewFramework, 'id' | 'createdDate' | 'modifiedDate' | 'frameworkTypes' | 'fileUploads'>;

type FrameworkFormGroupContent = {
  id: FormControl<FrameworkFormRawValue['id'] | NewFramework['id']>;
  name: FormControl<FrameworkFormRawValue['name']>;
  createdDate: FormControl<FrameworkFormRawValue['createdDate']>;
  modifiedDate: FormControl<FrameworkFormRawValue['modifiedDate']>;
  frameworkTypes: FormControl<FrameworkFormRawValue['frameworkTypes']>;
  fileUploads: FormControl<FrameworkFormRawValue['fileUploads']>;
};

export type FrameworkFormGroup = FormGroup<FrameworkFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FrameworkFormService {
  createFrameworkFormGroup(framework: FrameworkFormGroupInput = { id: null }): FrameworkFormGroup {
    const frameworkRawValue = this.convertFrameworkToFrameworkRawValue({
      ...this.getFormDefaults(),
      ...framework,
    });
    return new FormGroup<FrameworkFormGroupContent>({
      id: new FormControl(
        { value: frameworkRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(frameworkRawValue.name, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(frameworkRawValue.createdDate),
      modifiedDate: new FormControl(frameworkRawValue.modifiedDate),
      frameworkTypes: new FormControl(frameworkRawValue.frameworkTypes ?? []),
      fileUploads: new FormControl(frameworkRawValue.fileUploads ?? []),
    });
  }

  getFramework(form: FrameworkFormGroup): IFramework | NewFramework {
    return this.convertFrameworkRawValueToFramework(form.getRawValue() as FrameworkFormRawValue | NewFrameworkFormRawValue);
  }

  resetForm(form: FrameworkFormGroup, framework: FrameworkFormGroupInput): void {
    const frameworkRawValue = this.convertFrameworkToFrameworkRawValue({ ...this.getFormDefaults(), ...framework });
    form.reset(
      {
        ...frameworkRawValue,
        id: { value: frameworkRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FrameworkFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      modifiedDate: currentTime,
      frameworkTypes: [],
      fileUploads: [],
    };
  }

  private convertFrameworkRawValueToFramework(rawFramework: FrameworkFormRawValue | NewFrameworkFormRawValue): IFramework | NewFramework {
    return {
      ...rawFramework,
      createdDate: dayjs(rawFramework.createdDate, DATE_TIME_FORMAT),
      modifiedDate: dayjs(rawFramework.modifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertFrameworkToFrameworkRawValue(
    framework: IFramework | (Partial<NewFramework> & FrameworkFormDefaults)
  ): FrameworkFormRawValue | PartialWithRequiredKeyOf<NewFrameworkFormRawValue> {
    return {
      ...framework,
      createdDate: framework.createdDate ? framework.createdDate.format(DATE_TIME_FORMAT) : undefined,
      modifiedDate: framework.modifiedDate ? framework.modifiedDate.format(DATE_TIME_FORMAT) : undefined,
      frameworkTypes: framework.frameworkTypes ?? [],
      fileUploads: framework.fileUploads ?? [],
    };
  }
}
