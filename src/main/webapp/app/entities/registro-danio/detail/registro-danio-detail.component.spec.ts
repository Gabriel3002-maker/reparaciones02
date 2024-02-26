import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RegistroDanioDetailComponent } from './registro-danio-detail.component';

describe('RegistroDanio Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistroDanioDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: RegistroDanioDetailComponent,
              resolve: { registroDanio: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RegistroDanioDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load registroDanio on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RegistroDanioDetailComponent);

      // THEN
      expect(instance.registroDanio).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
