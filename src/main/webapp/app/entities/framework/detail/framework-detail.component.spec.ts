import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FrameworkDetailComponent } from './framework-detail.component';

describe('Framework Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FrameworkDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FrameworkDetailComponent,
              resolve: { framework: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(FrameworkDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load framework on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FrameworkDetailComponent);

      // THEN
      expect(instance.framework).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
