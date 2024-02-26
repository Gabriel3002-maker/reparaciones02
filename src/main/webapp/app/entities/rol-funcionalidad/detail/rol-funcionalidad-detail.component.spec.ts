import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RolFuncionalidadDetailComponent } from './rol-funcionalidad-detail.component';

describe('RolFuncionalidad Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RolFuncionalidadDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: RolFuncionalidadDetailComponent,
              resolve: { rolFuncionalidad: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RolFuncionalidadDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load rolFuncionalidad on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RolFuncionalidadDetailComponent);

      // THEN
      expect(instance.rolFuncionalidad).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
