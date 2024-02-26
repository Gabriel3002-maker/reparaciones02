import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OrdenBodegaDetailComponent } from './orden-bodega-detail.component';

describe('OrdenBodega Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrdenBodegaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: OrdenBodegaDetailComponent,
              resolve: { ordenBodega: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(OrdenBodegaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load ordenBodega on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', OrdenBodegaDetailComponent);

      // THEN
      expect(instance.ordenBodega).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
