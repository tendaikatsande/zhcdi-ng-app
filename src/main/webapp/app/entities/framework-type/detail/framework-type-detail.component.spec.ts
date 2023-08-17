import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FrameworkTypeDetailComponent } from './framework-type-detail.component';

describe('FrameworkType Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FrameworkTypeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FrameworkTypeDetailComponent,
              resolve: { frameworkType: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(FrameworkTypeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load frameworkType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FrameworkTypeDetailComponent);

      // THEN
      expect(instance.frameworkType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
