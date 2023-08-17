import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../framework.test-samples';

import { FrameworkFormService } from './framework-form.service';

describe('Framework Form Service', () => {
  let service: FrameworkFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FrameworkFormService);
  });

  describe('Service methods', () => {
    describe('createFrameworkFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFrameworkFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            frameworkTypes: expect.any(Object),
            fileUploads: expect.any(Object),
          })
        );
      });

      it('passing IFramework should create a new form with FormGroup', () => {
        const formGroup = service.createFrameworkFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            frameworkTypes: expect.any(Object),
            fileUploads: expect.any(Object),
          })
        );
      });
    });

    describe('getFramework', () => {
      it('should return NewFramework for default Framework initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFrameworkFormGroup(sampleWithNewData);

        const framework = service.getFramework(formGroup) as any;

        expect(framework).toMatchObject(sampleWithNewData);
      });

      it('should return NewFramework for empty Framework initial value', () => {
        const formGroup = service.createFrameworkFormGroup();

        const framework = service.getFramework(formGroup) as any;

        expect(framework).toMatchObject({});
      });

      it('should return IFramework', () => {
        const formGroup = service.createFrameworkFormGroup(sampleWithRequiredData);

        const framework = service.getFramework(formGroup) as any;

        expect(framework).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFramework should not enable id FormControl', () => {
        const formGroup = service.createFrameworkFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFramework should disable id FormControl', () => {
        const formGroup = service.createFrameworkFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
