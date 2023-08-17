import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFramework } from '../framework.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../framework.test-samples';

import { FrameworkService, RestFramework } from './framework.service';

const requireRestSample: RestFramework = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  modifiedDate: sampleWithRequiredData.modifiedDate?.toJSON(),
};

describe('Framework Service', () => {
  let service: FrameworkService;
  let httpMock: HttpTestingController;
  let expectedResult: IFramework | IFramework[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FrameworkService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Framework', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const framework = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(framework).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Framework', () => {
      const framework = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(framework).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Framework', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Framework', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Framework', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFrameworkToCollectionIfMissing', () => {
      it('should add a Framework to an empty array', () => {
        const framework: IFramework = sampleWithRequiredData;
        expectedResult = service.addFrameworkToCollectionIfMissing([], framework);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(framework);
      });

      it('should not add a Framework to an array that contains it', () => {
        const framework: IFramework = sampleWithRequiredData;
        const frameworkCollection: IFramework[] = [
          {
            ...framework,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFrameworkToCollectionIfMissing(frameworkCollection, framework);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Framework to an array that doesn't contain it", () => {
        const framework: IFramework = sampleWithRequiredData;
        const frameworkCollection: IFramework[] = [sampleWithPartialData];
        expectedResult = service.addFrameworkToCollectionIfMissing(frameworkCollection, framework);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(framework);
      });

      it('should add only unique Framework to an array', () => {
        const frameworkArray: IFramework[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const frameworkCollection: IFramework[] = [sampleWithRequiredData];
        expectedResult = service.addFrameworkToCollectionIfMissing(frameworkCollection, ...frameworkArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const framework: IFramework = sampleWithRequiredData;
        const framework2: IFramework = sampleWithPartialData;
        expectedResult = service.addFrameworkToCollectionIfMissing([], framework, framework2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(framework);
        expect(expectedResult).toContain(framework2);
      });

      it('should accept null and undefined values', () => {
        const framework: IFramework = sampleWithRequiredData;
        expectedResult = service.addFrameworkToCollectionIfMissing([], null, framework, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(framework);
      });

      it('should return initial array if no Framework is added', () => {
        const frameworkCollection: IFramework[] = [sampleWithRequiredData];
        expectedResult = service.addFrameworkToCollectionIfMissing(frameworkCollection, undefined, null);
        expect(expectedResult).toEqual(frameworkCollection);
      });
    });

    describe('compareFramework', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFramework(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFramework(entity1, entity2);
        const compareResult2 = service.compareFramework(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFramework(entity1, entity2);
        const compareResult2 = service.compareFramework(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFramework(entity1, entity2);
        const compareResult2 = service.compareFramework(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
