import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DetalleDanioDetailComponent } from './detalle-danio-detail.component';

describe('DetalleDanio Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalleDanioDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DetalleDanioDetailComponent,
              resolve: { detalleDanio: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DetalleDanioDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load detalleDanio on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DetalleDanioDetailComponent);

      // THEN
      expect(instance.detalleDanio).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
