import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MaterialDanioService } from '../service/material-danio.service';
import { IMaterialDanio } from '../material-danio.model';
import { MaterialDanioFormService } from './material-danio-form.service';

import { MaterialDanioUpdateComponent } from './material-danio-update.component';

describe('MaterialDanio Management Update Component', () => {
  let comp: MaterialDanioUpdateComponent;
  let fixture: ComponentFixture<MaterialDanioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let materialDanioFormService: MaterialDanioFormService;
  let materialDanioService: MaterialDanioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MaterialDanioUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MaterialDanioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaterialDanioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    materialDanioFormService = TestBed.inject(MaterialDanioFormService);
    materialDanioService = TestBed.inject(MaterialDanioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const materialDanio: IMaterialDanio = { id: 456 };

      activatedRoute.data = of({ materialDanio });
      comp.ngOnInit();

      expect(comp.materialDanio).toEqual(materialDanio);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialDanio>>();
      const materialDanio = { id: 123 };
      jest.spyOn(materialDanioFormService, 'getMaterialDanio').mockReturnValue(materialDanio);
      jest.spyOn(materialDanioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialDanio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materialDanio }));
      saveSubject.complete();

      // THEN
      expect(materialDanioFormService.getMaterialDanio).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(materialDanioService.update).toHaveBeenCalledWith(expect.objectContaining(materialDanio));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialDanio>>();
      const materialDanio = { id: 123 };
      jest.spyOn(materialDanioFormService, 'getMaterialDanio').mockReturnValue({ id: null });
      jest.spyOn(materialDanioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialDanio: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materialDanio }));
      saveSubject.complete();

      // THEN
      expect(materialDanioFormService.getMaterialDanio).toHaveBeenCalled();
      expect(materialDanioService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialDanio>>();
      const materialDanio = { id: 123 };
      jest.spyOn(materialDanioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialDanio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(materialDanioService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
