import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CatalogoItemDetailComponent } from './catalogo-item-detail.component';

describe('CatalogoItem Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CatalogoItemDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CatalogoItemDetailComponent,
              resolve: { catalogoItem: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CatalogoItemDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load catalogoItem on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CatalogoItemDetailComponent);

      // THEN
      expect(instance.catalogoItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
