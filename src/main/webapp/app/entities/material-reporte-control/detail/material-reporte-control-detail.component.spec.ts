import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MaterialReporteControlDetailComponent } from './material-reporte-control-detail.component';

describe('MaterialReporteControl Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MaterialReporteControlDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MaterialReporteControlDetailComponent,
              resolve: { materialReporteControl: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MaterialReporteControlDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load materialReporteControl on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MaterialReporteControlDetailComponent);

      // THEN
      expect(instance.materialReporteControl).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
