import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FileUploadFormService } from './file-upload-form.service';
import { FileUploadService } from '../service/file-upload.service';
import { IFileUpload } from '../file-upload.model';

import { FileUploadUpdateComponent } from './file-upload-update.component';

describe('FileUpload Management Update Component', () => {
  let comp: FileUploadUpdateComponent;
  let fixture: ComponentFixture<FileUploadUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fileUploadFormService: FileUploadFormService;
  let fileUploadService: FileUploadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FileUploadUpdateComponent],
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
      .overrideTemplate(FileUploadUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FileUploadUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fileUploadFormService = TestBed.inject(FileUploadFormService);
    fileUploadService = TestBed.inject(FileUploadService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const fileUpload: IFileUpload = { id: 456 };

      activatedRoute.data = of({ fileUpload });
      comp.ngOnInit();

      expect(comp.fileUpload).toEqual(fileUpload);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFileUpload>>();
      const fileUpload = { id: 123 };
      jest.spyOn(fileUploadFormService, 'getFileUpload').mockReturnValue(fileUpload);
      jest.spyOn(fileUploadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileUpload });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fileUpload }));
      saveSubject.complete();

      // THEN
      expect(fileUploadFormService.getFileUpload).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fileUploadService.update).toHaveBeenCalledWith(expect.objectContaining(fileUpload));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFileUpload>>();
      const fileUpload = { id: 123 };
      jest.spyOn(fileUploadFormService, 'getFileUpload').mockReturnValue({ id: null });
      jest.spyOn(fileUploadService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileUpload: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fileUpload }));
      saveSubject.complete();

      // THEN
      expect(fileUploadFormService.getFileUpload).toHaveBeenCalled();
      expect(fileUploadService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFileUpload>>();
      const fileUpload = { id: 123 };
      jest.spyOn(fileUploadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileUpload });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fileUploadService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
