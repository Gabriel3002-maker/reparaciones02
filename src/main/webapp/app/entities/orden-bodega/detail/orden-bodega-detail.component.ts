import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IOrdenBodega } from '../orden-bodega.model';

@Component({
  standalone: true,
  selector: 'jhi-orden-bodega-detail',
  templateUrl: './orden-bodega-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class OrdenBodegaDetailComponent {
  @Input() ordenBodega: IOrdenBodega | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
