import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../cso.test-samples';

import { CsoFormService } from './cso-form.service';

describe('Cso Form Service', () => {
  let service: CsoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CsoFormService);
  });

  describe('Service methods', () => {
    describe('createCsoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCsoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            organisation: expect.any(Object),
            cell: expect.any(Object),
            city: expect.any(Object),
            email: expect.any(Object),
            registrationCertificate: expect.any(Object),
            organisationProfile: expect.any(Object),
            managementStructure: expect.any(Object),
            strategicPlan: expect.any(Object),
            resourceMobilisationPlan: expect.any(Object),
            comments: expect.any(Object),
            enquiries: expect.any(Object),
            lat: expect.any(Object),
            lng: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            provinces: expect.any(Object),
          })
        );
      });

      it('passing ICso should create a new form with FormGroup', () => {
        const formGroup = service.createCsoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            organisation: expect.any(Object),
            cell: expect.any(Object),
            city: expect.any(Object),
            email: expect.any(Object),
            registrationCertificate: expect.any(Object),
            organisationProfile: expect.any(Object),
            managementStructure: expect.any(Object),
            strategicPlan: expect.any(Object),
            resourceMobilisationPlan: expect.any(Object),
            comments: expect.any(Object),
            enquiries: expect.any(Object),
            lat: expect.any(Object),
            lng: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            provinces: expect.any(Object),
          })
        );
      });
    });

    describe('getCso', () => {
      it('should return NewCso for default Cso initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCsoFormGroup(sampleWithNewData);

        const cso = service.getCso(formGroup) as any;

        expect(cso).toMatchObject(sampleWithNewData);
      });

      it('should return NewCso for empty Cso initial value', () => {
        const formGroup = service.createCsoFormGroup();

        const cso = service.getCso(formGroup) as any;

        expect(cso).toMatchObject({});
      });

      it('should return ICso', () => {
        const formGroup = service.createCsoFormGroup(sampleWithRequiredData);

        const cso = service.getCso(formGroup) as any;

        expect(cso).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICso should not enable id FormControl', () => {
        const formGroup = service.createCsoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCso should disable id FormControl', () => {
        const formGroup = service.createCsoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
