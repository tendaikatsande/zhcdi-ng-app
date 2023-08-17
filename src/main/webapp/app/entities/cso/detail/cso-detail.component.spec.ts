import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CsoDetailComponent } from './cso-detail.component';

describe('Cso Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CsoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CsoDetailComponent,
              resolve: { cso: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(CsoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load cso on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CsoDetailComponent);

      // THEN
      expect(instance.cso).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
