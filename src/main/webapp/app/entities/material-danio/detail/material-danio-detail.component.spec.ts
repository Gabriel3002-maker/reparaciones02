import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MaterialDanioDetailComponent } from './material-danio-detail.component';

describe('MaterialDanio Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MaterialDanioDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MaterialDanioDetailComponent,
              resolve: { materialDanio: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MaterialDanioDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load materialDanio on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MaterialDanioDetailComponent);

      // THEN
      expect(instance.materialDanio).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
