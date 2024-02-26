import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FuncionalidadDetailComponent } from './funcionalidad-detail.component';

describe('Funcionalidad Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FuncionalidadDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FuncionalidadDetailComponent,
              resolve: { funcionalidad: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FuncionalidadDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load funcionalidad on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FuncionalidadDetailComponent);

      // THEN
      expect(instance.funcionalidad).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
