import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IDetalleReporteDanio } from '../detalle-reporte-danio.model';

@Component({
  standalone: true,
  selector: 'jhi-detalle-reporte-danio-detail',
  templateUrl: './detalle-reporte-danio-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DetalleReporteDanioDetailComponent {
  @Input() detalleReporteDanio: IDetalleReporteDanio | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
