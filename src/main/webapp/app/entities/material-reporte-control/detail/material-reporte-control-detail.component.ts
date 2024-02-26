import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IMaterialReporteControl } from '../material-reporte-control.model';

@Component({
  standalone: true,
  selector: 'jhi-material-reporte-control-detail',
  templateUrl: './material-reporte-control-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MaterialReporteControlDetailComponent {
  @Input() materialReporteControl: IMaterialReporteControl | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
