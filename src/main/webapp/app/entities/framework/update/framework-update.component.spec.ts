import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FrameworkFormService } from './framework-form.service';
import { FrameworkService } from '../service/framework.service';
import { IFramework } from '../framework.model';
import { IFrameworkType } from 'app/entities/framework-type/framework-type.model';
import { FrameworkTypeService } from 'app/entities/framework-type/service/framework-type.service';
import { IFileUpload } from 'app/entities/file-upload/file-upload.model';
import { FileUploadService } from 'app/entities/file-upload/service/file-upload.service';

import { FrameworkUpdateComponent } from './framework-update.component';

describe('Framework Management Update Component', () => {
  let comp: FrameworkUpdateComponent;
  let fixture: ComponentFixture<FrameworkUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let frameworkFormService: FrameworkFormService;
  let frameworkService: FrameworkService;
  let frameworkTypeService: FrameworkTypeService;
  let fileUploadService: FileUploadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FrameworkUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FrameworkUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FrameworkUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    frameworkFormService = TestBed.inject(FrameworkFormService);
    frameworkService = TestBed.inject(FrameworkService);
    frameworkTypeService = TestBed.inject(FrameworkTypeService);
    fileUploadService = TestBed.inject(FileUploadService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call FrameworkType query and add missing value', () => {
      const framework: IFramework = { id: 456 };
      const frameworkTypes: IFrameworkType[] = [{ id: 29813 }];
      framework.frameworkTypes = frameworkTypes;

      const frameworkTypeCollection: IFrameworkType[] = [{ id: 20582 }];
      jest.spyOn(frameworkTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: frameworkTypeCollection })));
      const additionalFrameworkTypes = [...frameworkTypes];
      const expectedCollection: IFrameworkType[] = [...additionalFrameworkTypes, ...frameworkTypeCollection];
      jest.spyOn(frameworkTypeService, 'addFrameworkTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ framework });
      comp.ngOnInit();

      expect(frameworkTypeService.query).toHaveBeenCalled();
      expect(frameworkTypeService.addFrameworkTypeToCollectionIfMissing).toHaveBeenCalledWith(
        frameworkTypeCollection,
        ...additionalFrameworkTypes.map(expect.objectContaining)
      );
      expect(comp.frameworkTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FileUpload query and add missing value', () => {
      const framework: IFramework = { id: 456 };
      const fileUploads: IFileUpload[] = [{ id: 32075 }];
      framework.fileUploads = fileUploads;

      const fileUploadCollection: IFileUpload[] = [{ id: 20446 }];
      jest.spyOn(fileUploadService, 'query').mockReturnValue(of(new HttpResponse({ body: fileUploadCollection })));
      const additionalFileUploads = [...fileUploads];
      const expectedCollection: IFileUpload[] = [...additionalFileUploads, ...fileUploadCollection];
      jest.spyOn(fileUploadService, 'addFileUploadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ framework });
      comp.ngOnInit();

      expect(fileUploadService.query).toHaveBeenCalled();
      expect(fileUploadService.addFileUploadToCollectionIfMissing).toHaveBeenCalledWith(
        fileUploadCollection,
        ...additionalFileUploads.map(expect.objectContaining)
      );
      expect(comp.fileUploadsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const framework: IFramework = { id: 456 };
      const frameworkType: IFrameworkType = { id: 11215 };
      framework.frameworkTypes = [frameworkType];
      const fileUpload: IFileUpload = { id: 18595 };
      framework.fileUploads = [fileUpload];

      activatedRoute.data = of({ framework });
      comp.ngOnInit();

      expect(comp.frameworkTypesSharedCollection).toContain(frameworkType);
      expect(comp.fileUploadsSharedCollection).toContain(fileUpload);
      expect(comp.framework).toEqual(framework);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFramework>>();
      const framework = { id: 123 };
      jest.spyOn(frameworkFormService, 'getFramework').mockReturnValue(framework);
      jest.spyOn(frameworkService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ framework });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: framework }));
      saveSubject.complete();

      // THEN
      expect(frameworkFormService.getFramework).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(frameworkService.update).toHaveBeenCalledWith(expect.objectContaining(framework));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFramework>>();
      const framework = { id: 123 };
      jest.spyOn(frameworkFormService, 'getFramework').mockReturnValue({ id: null });
      jest.spyOn(frameworkService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ framework: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: framework }));
      saveSubject.complete();

      // THEN
      expect(frameworkFormService.getFramework).toHaveBeenCalled();
      expect(frameworkService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFramework>>();
      const framework = { id: 123 };
      jest.spyOn(frameworkService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ framework });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(frameworkService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFrameworkType', () => {
      it('Should forward to frameworkTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(frameworkTypeService, 'compareFrameworkType');
        comp.compareFrameworkType(entity, entity2);
        expect(frameworkTypeService.compareFrameworkType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFileUpload', () => {
      it('Should forward to fileUploadService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(fileUploadService, 'compareFileUpload');
        comp.compareFileUpload(entity, entity2);
        expect(fileUploadService.compareFileUpload).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
