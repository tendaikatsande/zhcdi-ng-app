import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CsoFormService } from './cso-form.service';
import { CsoService } from '../service/cso.service';
import { ICso } from '../cso.model';
import { IProvince } from 'app/entities/province/province.model';
import { ProvinceService } from 'app/entities/province/service/province.service';

import { CsoUpdateComponent } from './cso-update.component';

describe('Cso Management Update Component', () => {
  let comp: CsoUpdateComponent;
  let fixture: ComponentFixture<CsoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let csoFormService: CsoFormService;
  let csoService: CsoService;
  let provinceService: ProvinceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CsoUpdateComponent],
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
      .overrideTemplate(CsoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CsoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    csoFormService = TestBed.inject(CsoFormService);
    csoService = TestBed.inject(CsoService);
    provinceService = TestBed.inject(ProvinceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Province query and add missing value', () => {
      const cso: ICso = { id: 456 };
      const provinces: IProvince[] = [{ id: 12896 }];
      cso.provinces = provinces;

      const provinceCollection: IProvince[] = [{ id: 16711 }];
      jest.spyOn(provinceService, 'query').mockReturnValue(of(new HttpResponse({ body: provinceCollection })));
      const additionalProvinces = [...provinces];
      const expectedCollection: IProvince[] = [...additionalProvinces, ...provinceCollection];
      jest.spyOn(provinceService, 'addProvinceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cso });
      comp.ngOnInit();

      expect(provinceService.query).toHaveBeenCalled();
      expect(provinceService.addProvinceToCollectionIfMissing).toHaveBeenCalledWith(
        provinceCollection,
        ...additionalProvinces.map(expect.objectContaining)
      );
      expect(comp.provincesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cso: ICso = { id: 456 };
      const province: IProvince = { id: 23006 };
      cso.provinces = [province];

      activatedRoute.data = of({ cso });
      comp.ngOnInit();

      expect(comp.provincesSharedCollection).toContain(province);
      expect(comp.cso).toEqual(cso);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICso>>();
      const cso = { id: 123 };
      jest.spyOn(csoFormService, 'getCso').mockReturnValue(cso);
      jest.spyOn(csoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cso }));
      saveSubject.complete();

      // THEN
      expect(csoFormService.getCso).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(csoService.update).toHaveBeenCalledWith(expect.objectContaining(cso));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICso>>();
      const cso = { id: 123 };
      jest.spyOn(csoFormService, 'getCso').mockReturnValue({ id: null });
      jest.spyOn(csoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cso: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cso }));
      saveSubject.complete();

      // THEN
      expect(csoFormService.getCso).toHaveBeenCalled();
      expect(csoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICso>>();
      const cso = { id: 123 };
      jest.spyOn(csoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(csoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProvince', () => {
      it('Should forward to provinceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(provinceService, 'compareProvince');
        comp.compareProvince(entity, entity2);
        expect(provinceService.compareProvince).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
