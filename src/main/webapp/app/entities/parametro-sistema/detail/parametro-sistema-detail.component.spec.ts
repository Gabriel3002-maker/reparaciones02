import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ParametroSistemaDetailComponent } from './parametro-sistema-detail.component';

describe('ParametroSistema Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ParametroSistemaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ParametroSistemaDetailComponent,
              resolve: { parametroSistema: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ParametroSistemaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load parametroSistema on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ParametroSistemaDetailComponent);

      // THEN
      expect(instance.parametroSistema).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
