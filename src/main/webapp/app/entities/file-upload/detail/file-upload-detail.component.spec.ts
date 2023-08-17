import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FileUploadDetailComponent } from './file-upload-detail.component';

describe('FileUpload Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FileUploadDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FileUploadDetailComponent,
              resolve: { fileUpload: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(FileUploadDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load fileUpload on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FileUploadDetailComponent);

      // THEN
      expect(instance.fileUpload).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
