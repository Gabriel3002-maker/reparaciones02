import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IParametroSistema } from '../parametro-sistema.model';

@Component({
  standalone: true,
  selector: 'jhi-parametro-sistema-detail',
  templateUrl: './parametro-sistema-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ParametroSistemaDetailComponent {
  @Input() parametroSistema: IParametroSistema | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
