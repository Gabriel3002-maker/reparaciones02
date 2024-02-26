import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DetalleReporteDanioDetailComponent } from './detalle-reporte-danio-detail.component';

describe('DetalleReporteDanio Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalleReporteDanioDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DetalleReporteDanioDetailComponent,
              resolve: { detalleReporteDanio: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DetalleReporteDanioDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load detalleReporteDanio on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DetalleReporteDanioDetailComponent);

      // THEN
      expect(instance.detalleReporteDanio).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
