import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICso } from '../cso.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../cso.test-samples';

import { CsoService, RestCso } from './cso.service';

const requireRestSample: RestCso = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  modifiedDate: sampleWithRequiredData.modifiedDate?.toJSON(),
};

describe('Cso Service', () => {
  let service: CsoService;
  let httpMock: HttpTestingController;
  let expectedResult: ICso | ICso[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CsoService);
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

    it('should create a Cso', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const cso = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cso).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cso', () => {
      const cso = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cso).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cso', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cso', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Cso', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCsoToCollectionIfMissing', () => {
      it('should add a Cso to an empty array', () => {
        const cso: ICso = sampleWithRequiredData;
        expectedResult = service.addCsoToCollectionIfMissing([], cso);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cso);
      });

      it('should not add a Cso to an array that contains it', () => {
        const cso: ICso = sampleWithRequiredData;
        const csoCollection: ICso[] = [
          {
            ...cso,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCsoToCollectionIfMissing(csoCollection, cso);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cso to an array that doesn't contain it", () => {
        const cso: ICso = sampleWithRequiredData;
        const csoCollection: ICso[] = [sampleWithPartialData];
        expectedResult = service.addCsoToCollectionIfMissing(csoCollection, cso);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cso);
      });

      it('should add only unique Cso to an array', () => {
        const csoArray: ICso[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const csoCollection: ICso[] = [sampleWithRequiredData];
        expectedResult = service.addCsoToCollectionIfMissing(csoCollection, ...csoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cso: ICso = sampleWithRequiredData;
        const cso2: ICso = sampleWithPartialData;
        expectedResult = service.addCsoToCollectionIfMissing([], cso, cso2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cso);
        expect(expectedResult).toContain(cso2);
      });

      it('should accept null and undefined values', () => {
        const cso: ICso = sampleWithRequiredData;
        expectedResult = service.addCsoToCollectionIfMissing([], null, cso, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cso);
      });

      it('should return initial array if no Cso is added', () => {
        const csoCollection: ICso[] = [sampleWithRequiredData];
        expectedResult = service.addCsoToCollectionIfMissing(csoCollection, undefined, null);
        expect(expectedResult).toEqual(csoCollection);
      });
    });

    describe('compareCso', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCso(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCso(entity1, entity2);
        const compareResult2 = service.compareCso(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCso(entity1, entity2);
        const compareResult2 = service.compareCso(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCso(entity1, entity2);
        const compareResult2 = service.compareCso(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
