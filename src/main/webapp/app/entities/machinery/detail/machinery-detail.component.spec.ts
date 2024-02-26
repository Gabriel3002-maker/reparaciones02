import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MachineryDetailComponent } from './machinery-detail.component';

describe('Machinery Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MachineryDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MachineryDetailComponent,
              resolve: { machinery: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MachineryDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load machinery on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MachineryDetailComponent);

      // THEN
      expect(instance.machinery).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
