import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProvince, NewProvince } from '../province.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProvince for edit and NewProvinceFormGroupInput for create.
 */
type ProvinceFormGroupInput = IProvince | PartialWithRequiredKeyOf<NewProvince>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProvince | NewProvince> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

type ProvinceFormRawValue = FormValueOf<IProvince>;

type NewProvinceFormRawValue = FormValueOf<NewProvince>;

type ProvinceFormDefaults = Pick<NewProvince, 'id' | 'createdDate' | 'modifiedDate' | 'csos'>;

type ProvinceFormGroupContent = {
  id: FormControl<ProvinceFormRawValue['id'] | NewProvince['id']>;
  name: FormControl<ProvinceFormRawValue['name']>;
  createdDate: FormControl<ProvinceFormRawValue['createdDate']>;
  modifiedDate: FormControl<ProvinceFormRawValue['modifiedDate']>;
  csos: FormControl<ProvinceFormRawValue['csos']>;
};

export type ProvinceFormGroup = FormGroup<ProvinceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProvinceFormService {
  createProvinceFormGroup(province: ProvinceFormGroupInput = { id: null }): ProvinceFormGroup {
    const provinceRawValue = this.convertProvinceToProvinceRawValue({
      ...this.getFormDefaults(),
      ...province,
    });
    return new FormGroup<ProvinceFormGroupContent>({
      id: new FormControl(
        { value: provinceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(provinceRawValue.name, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(provinceRawValue.createdDate),
      modifiedDate: new FormControl(provinceRawValue.modifiedDate),
      csos: new FormControl(provinceRawValue.csos ?? []),
    });
  }

  getProvince(form: ProvinceFormGroup): IProvince | NewProvince {
    return this.convertProvinceRawValueToProvince(form.getRawValue() as ProvinceFormRawValue | NewProvinceFormRawValue);
  }

  resetForm(form: ProvinceFormGroup, province: ProvinceFormGroupInput): void {
    const provinceRawValue = this.convertProvinceToProvinceRawValue({ ...this.getFormDefaults(), ...province });
    form.reset(
      {
        ...provinceRawValue,
        id: { value: provinceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProvinceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      modifiedDate: currentTime,
      csos: [],
    };
  }

  private convertProvinceRawValueToProvince(rawProvince: ProvinceFormRawValue | NewProvinceFormRawValue): IProvince | NewProvince {
    return {
      ...rawProvince,
      createdDate: dayjs(rawProvince.createdDate, DATE_TIME_FORMAT),
      modifiedDate: dayjs(rawProvince.modifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertProvinceToProvinceRawValue(
    province: IProvince | (Partial<NewProvince> & ProvinceFormDefaults)
  ): ProvinceFormRawValue | PartialWithRequiredKeyOf<NewProvinceFormRawValue> {
    return {
      ...province,
      createdDate: province.createdDate ? province.createdDate.format(DATE_TIME_FORMAT) : undefined,
      modifiedDate: province.modifiedDate ? province.modifiedDate.format(DATE_TIME_FORMAT) : undefined,
      csos: province.csos ?? [],
    };
  }
}
