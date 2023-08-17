import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFrameworkType, NewFrameworkType } from '../framework-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFrameworkType for edit and NewFrameworkTypeFormGroupInput for create.
 */
type FrameworkTypeFormGroupInput = IFrameworkType | PartialWithRequiredKeyOf<NewFrameworkType>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFrameworkType | NewFrameworkType> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

type FrameworkTypeFormRawValue = FormValueOf<IFrameworkType>;

type NewFrameworkTypeFormRawValue = FormValueOf<NewFrameworkType>;

type FrameworkTypeFormDefaults = Pick<NewFrameworkType, 'id' | 'createdDate' | 'modifiedDate' | 'frameworks'>;

type FrameworkTypeFormGroupContent = {
  id: FormControl<FrameworkTypeFormRawValue['id'] | NewFrameworkType['id']>;
  name: FormControl<FrameworkTypeFormRawValue['name']>;
  createdDate: FormControl<FrameworkTypeFormRawValue['createdDate']>;
  modifiedDate: FormControl<FrameworkTypeFormRawValue['modifiedDate']>;
  frameworks: FormControl<FrameworkTypeFormRawValue['frameworks']>;
};

export type FrameworkTypeFormGroup = FormGroup<FrameworkTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FrameworkTypeFormService {
  createFrameworkTypeFormGroup(frameworkType: FrameworkTypeFormGroupInput = { id: null }): FrameworkTypeFormGroup {
    const frameworkTypeRawValue = this.convertFrameworkTypeToFrameworkTypeRawValue({
      ...this.getFormDefaults(),
      ...frameworkType,
    });
    return new FormGroup<FrameworkTypeFormGroupContent>({
      id: new FormControl(
        { value: frameworkTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(frameworkTypeRawValue.name, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(frameworkTypeRawValue.createdDate),
      modifiedDate: new FormControl(frameworkTypeRawValue.modifiedDate),
      frameworks: new FormControl(frameworkTypeRawValue.frameworks ?? []),
    });
  }

  getFrameworkType(form: FrameworkTypeFormGroup): IFrameworkType | NewFrameworkType {
    return this.convertFrameworkTypeRawValueToFrameworkType(form.getRawValue() as FrameworkTypeFormRawValue | NewFrameworkTypeFormRawValue);
  }

  resetForm(form: FrameworkTypeFormGroup, frameworkType: FrameworkTypeFormGroupInput): void {
    const frameworkTypeRawValue = this.convertFrameworkTypeToFrameworkTypeRawValue({ ...this.getFormDefaults(), ...frameworkType });
    form.reset(
      {
        ...frameworkTypeRawValue,
        id: { value: frameworkTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FrameworkTypeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      modifiedDate: currentTime,
      frameworks: [],
    };
  }

  private convertFrameworkTypeRawValueToFrameworkType(
    rawFrameworkType: FrameworkTypeFormRawValue | NewFrameworkTypeFormRawValue
  ): IFrameworkType | NewFrameworkType {
    return {
      ...rawFrameworkType,
      createdDate: dayjs(rawFrameworkType.createdDate, DATE_TIME_FORMAT),
      modifiedDate: dayjs(rawFrameworkType.modifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertFrameworkTypeToFrameworkTypeRawValue(
    frameworkType: IFrameworkType | (Partial<NewFrameworkType> & FrameworkTypeFormDefaults)
  ): FrameworkTypeFormRawValue | PartialWithRequiredKeyOf<NewFrameworkTypeFormRawValue> {
    return {
      ...frameworkType,
      createdDate: frameworkType.createdDate ? frameworkType.createdDate.format(DATE_TIME_FORMAT) : undefined,
      modifiedDate: frameworkType.modifiedDate ? frameworkType.modifiedDate.format(DATE_TIME_FORMAT) : undefined,
      frameworks: frameworkType.frameworks ?? [],
    };
  }
}
