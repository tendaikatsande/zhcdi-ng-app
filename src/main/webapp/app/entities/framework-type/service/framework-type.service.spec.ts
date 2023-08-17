import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFrameworkType } from '../framework-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../framework-type.test-samples';

import { FrameworkTypeService, RestFrameworkType } from './framework-type.service';

const requireRestSample: RestFrameworkType = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  modifiedDate: sampleWithRequiredData.modifiedDate?.toJSON(),
};

describe('FrameworkType Service', () => {
  let service: FrameworkTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IFrameworkType | IFrameworkType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FrameworkTypeService);
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

    it('should create a FrameworkType', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const frameworkType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(frameworkType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FrameworkType', () => {
      const frameworkType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(frameworkType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FrameworkType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FrameworkType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FrameworkType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFrameworkTypeToCollectionIfMissing', () => {
      it('should add a FrameworkType to an empty array', () => {
        const frameworkType: IFrameworkType = sampleWithRequiredData;
        expectedResult = service.addFrameworkTypeToCollectionIfMissing([], frameworkType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(frameworkType);
      });

      it('should not add a FrameworkType to an array that contains it', () => {
        const frameworkType: IFrameworkType = sampleWithRequiredData;
        const frameworkTypeCollection: IFrameworkType[] = [
          {
            ...frameworkType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFrameworkTypeToCollectionIfMissing(frameworkTypeCollection, frameworkType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FrameworkType to an array that doesn't contain it", () => {
        const frameworkType: IFrameworkType = sampleWithRequiredData;
        const frameworkTypeCollection: IFrameworkType[] = [sampleWithPartialData];
        expectedResult = service.addFrameworkTypeToCollectionIfMissing(frameworkTypeCollection, frameworkType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(frameworkType);
      });

      it('should add only unique FrameworkType to an array', () => {
        const frameworkTypeArray: IFrameworkType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const frameworkTypeCollection: IFrameworkType[] = [sampleWithRequiredData];
        expectedResult = service.addFrameworkTypeToCollectionIfMissing(frameworkTypeCollection, ...frameworkTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const frameworkType: IFrameworkType = sampleWithRequiredData;
        const frameworkType2: IFrameworkType = sampleWithPartialData;
        expectedResult = service.addFrameworkTypeToCollectionIfMissing([], frameworkType, frameworkType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(frameworkType);
        expect(expectedResult).toContain(frameworkType2);
      });

      it('should accept null and undefined values', () => {
        const frameworkType: IFrameworkType = sampleWithRequiredData;
        expectedResult = service.addFrameworkTypeToCollectionIfMissing([], null, frameworkType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(frameworkType);
      });

      it('should return initial array if no FrameworkType is added', () => {
        const frameworkTypeCollection: IFrameworkType[] = [sampleWithRequiredData];
        expectedResult = service.addFrameworkTypeToCollectionIfMissing(frameworkTypeCollection, undefined, null);
        expect(expectedResult).toEqual(frameworkTypeCollection);
      });
    });

    describe('compareFrameworkType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFrameworkType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFrameworkType(entity1, entity2);
        const compareResult2 = service.compareFrameworkType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFrameworkType(entity1, entity2);
        const compareResult2 = service.compareFrameworkType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFrameworkType(entity1, entity2);
        const compareResult2 = service.compareFrameworkType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
