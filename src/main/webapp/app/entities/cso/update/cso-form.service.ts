import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICso, NewCso } from '../cso.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICso for edit and NewCsoFormGroupInput for create.
 */
type CsoFormGroupInput = ICso | PartialWithRequiredKeyOf<NewCso>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICso | NewCso> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

type CsoFormRawValue = FormValueOf<ICso>;

type NewCsoFormRawValue = FormValueOf<NewCso>;

type CsoFormDefaults = Pick<
  NewCso,
  | 'id'
  | 'registrationCertificate'
  | 'organisationProfile'
  | 'managementStructure'
  | 'strategicPlan'
  | 'resourceMobilisationPlan'
  | 'createdDate'
  | 'modifiedDate'
  | 'provinces'
>;

type CsoFormGroupContent = {
  id: FormControl<CsoFormRawValue['id'] | NewCso['id']>;
  firstName: FormControl<CsoFormRawValue['firstName']>;
  lastName: FormControl<CsoFormRawValue['lastName']>;
  organisation: FormControl<CsoFormRawValue['organisation']>;
  cell: FormControl<CsoFormRawValue['cell']>;
  city: FormControl<CsoFormRawValue['city']>;
  email: FormControl<CsoFormRawValue['email']>;
  registrationCertificate: FormControl<CsoFormRawValue['registrationCertificate']>;
  organisationProfile: FormControl<CsoFormRawValue['organisationProfile']>;
  managementStructure: FormControl<CsoFormRawValue['managementStructure']>;
  strategicPlan: FormControl<CsoFormRawValue['strategicPlan']>;
  resourceMobilisationPlan: FormControl<CsoFormRawValue['resourceMobilisationPlan']>;
  comments: FormControl<CsoFormRawValue['comments']>;
  enquiries: FormControl<CsoFormRawValue['enquiries']>;
  lat: FormControl<CsoFormRawValue['lat']>;
  lng: FormControl<CsoFormRawValue['lng']>;
  createdDate: FormControl<CsoFormRawValue['createdDate']>;
  modifiedDate: FormControl<CsoFormRawValue['modifiedDate']>;
  provinces: FormControl<CsoFormRawValue['provinces']>;
};

export type CsoFormGroup = FormGroup<CsoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CsoFormService {
  createCsoFormGroup(cso: CsoFormGroupInput = { id: null }): CsoFormGroup {
    const csoRawValue = this.convertCsoToCsoRawValue({
      ...this.getFormDefaults(),
      ...cso,
    });
    return new FormGroup<CsoFormGroupContent>({
      id: new FormControl(
        { value: csoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      firstName: new FormControl(csoRawValue.firstName, {
        validators: [Validators.required],
      }),
      lastName: new FormControl(csoRawValue.lastName, {
        validators: [Validators.required],
      }),
      organisation: new FormControl(csoRawValue.organisation, {
        validators: [Validators.required],
      }),
      cell: new FormControl(csoRawValue.cell, {
        validators: [Validators.required],
      }),
      city: new FormControl(csoRawValue.city, {
        validators: [Validators.required],
      }),
      email: new FormControl(csoRawValue.email, {
        validators: [Validators.required],
      }),
      registrationCertificate: new FormControl(csoRawValue.registrationCertificate),
      organisationProfile: new FormControl(csoRawValue.organisationProfile),
      managementStructure: new FormControl(csoRawValue.managementStructure),
      strategicPlan: new FormControl(csoRawValue.strategicPlan),
      resourceMobilisationPlan: new FormControl(csoRawValue.resourceMobilisationPlan),
      comments: new FormControl(csoRawValue.comments),
      enquiries: new FormControl(csoRawValue.enquiries),
      lat: new FormControl(csoRawValue.lat, {
        validators: [Validators.required],
      }),
      lng: new FormControl(csoRawValue.lng, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(csoRawValue.createdDate),
      modifiedDate: new FormControl(csoRawValue.modifiedDate),
      provinces: new FormControl(csoRawValue.provinces ?? []),
    });
  }

  getCso(form: CsoFormGroup): ICso | NewCso {
    return this.convertCsoRawValueToCso(form.getRawValue() as CsoFormRawValue | NewCsoFormRawValue);
  }

  resetForm(form: CsoFormGroup, cso: CsoFormGroupInput): void {
    const csoRawValue = this.convertCsoToCsoRawValue({ ...this.getFormDefaults(), ...cso });
    form.reset(
      {
        ...csoRawValue,
        id: { value: csoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CsoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      registrationCertificate: false,
      organisationProfile: false,
      managementStructure: false,
      strategicPlan: false,
      resourceMobilisationPlan: false,
      createdDate: currentTime,
      modifiedDate: currentTime,
      provinces: [],
    };
  }

  private convertCsoRawValueToCso(rawCso: CsoFormRawValue | NewCsoFormRawValue): ICso | NewCso {
    return {
      ...rawCso,
      createdDate: dayjs(rawCso.createdDate, DATE_TIME_FORMAT),
      modifiedDate: dayjs(rawCso.modifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertCsoToCsoRawValue(
    cso: ICso | (Partial<NewCso> & CsoFormDefaults)
  ): CsoFormRawValue | PartialWithRequiredKeyOf<NewCsoFormRawValue> {
    return {
      ...cso,
      createdDate: cso.createdDate ? cso.createdDate.format(DATE_TIME_FORMAT) : undefined,
      modifiedDate: cso.modifiedDate ? cso.modifiedDate.format(DATE_TIME_FORMAT) : undefined,
      provinces: cso.provinces ?? [],
    };
  }
}
