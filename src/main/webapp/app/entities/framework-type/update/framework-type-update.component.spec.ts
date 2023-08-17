import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FrameworkTypeFormService } from './framework-type-form.service';
import { FrameworkTypeService } from '../service/framework-type.service';
import { IFrameworkType } from '../framework-type.model';

import { FrameworkTypeUpdateComponent } from './framework-type-update.component';

describe('FrameworkType Management Update Component', () => {
  let comp: FrameworkTypeUpdateComponent;
  let fixture: ComponentFixture<FrameworkTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let frameworkTypeFormService: FrameworkTypeFormService;
  let frameworkTypeService: FrameworkTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FrameworkTypeUpdateComponent],
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
      .overrideTemplate(FrameworkTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FrameworkTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    frameworkTypeFormService = TestBed.inject(FrameworkTypeFormService);
    frameworkTypeService = TestBed.inject(FrameworkTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const frameworkType: IFrameworkType = { id: 456 };

      activatedRoute.data = of({ frameworkType });
      comp.ngOnInit();

      expect(comp.frameworkType).toEqual(frameworkType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFrameworkType>>();
      const frameworkType = { id: 123 };
      jest.spyOn(frameworkTypeFormService, 'getFrameworkType').mockReturnValue(frameworkType);
      jest.spyOn(frameworkTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ frameworkType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: frameworkType }));
      saveSubject.complete();

      // THEN
      expect(frameworkTypeFormService.getFrameworkType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(frameworkTypeService.update).toHaveBeenCalledWith(expect.objectContaining(frameworkType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFrameworkType>>();
      const frameworkType = { id: 123 };
      jest.spyOn(frameworkTypeFormService, 'getFrameworkType').mockReturnValue({ id: null });
      jest.spyOn(frameworkTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ frameworkType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: frameworkType }));
      saveSubject.complete();

      // THEN
      expect(frameworkTypeFormService.getFrameworkType).toHaveBeenCalled();
      expect(frameworkTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFrameworkType>>();
      const frameworkType = { id: 123 };
      jest.spyOn(frameworkTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ frameworkType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(frameworkTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
