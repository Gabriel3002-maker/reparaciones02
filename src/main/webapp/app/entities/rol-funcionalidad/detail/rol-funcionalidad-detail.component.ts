import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IRolFuncionalidad } from '../rol-funcionalidad.model';

@Component({
  standalone: true,
  selector: 'jhi-rol-funcionalidad-detail',
  templateUrl: './rol-funcionalidad-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RolFuncionalidadDetailComponent {
  @Input() rolFuncionalidad: IRolFuncionalidad | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
