import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../framework-type.test-samples';

import { FrameworkTypeFormService } from './framework-type-form.service';

describe('FrameworkType Form Service', () => {
  let service: FrameworkTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FrameworkTypeFormService);
  });

  describe('Service methods', () => {
    describe('createFrameworkTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFrameworkTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            frameworks: expect.any(Object),
          })
        );
      });

      it('passing IFrameworkType should create a new form with FormGroup', () => {
        const formGroup = service.createFrameworkTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            frameworks: expect.any(Object),
          })
        );
      });
    });

    describe('getFrameworkType', () => {
      it('should return NewFrameworkType for default FrameworkType initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFrameworkTypeFormGroup(sampleWithNewData);

        const frameworkType = service.getFrameworkType(formGroup) as any;

        expect(frameworkType).toMatchObject(sampleWithNewData);
      });

      it('should return NewFrameworkType for empty FrameworkType initial value', () => {
        const formGroup = service.createFrameworkTypeFormGroup();

        const frameworkType = service.getFrameworkType(formGroup) as any;

        expect(frameworkType).toMatchObject({});
      });

      it('should return IFrameworkType', () => {
        const formGroup = service.createFrameworkTypeFormGroup(sampleWithRequiredData);

        const frameworkType = service.getFrameworkType(formGroup) as any;

        expect(frameworkType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFrameworkType should not enable id FormControl', () => {
        const formGroup = service.createFrameworkTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFrameworkType should disable id FormControl', () => {
        const formGroup = service.createFrameworkTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
