import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDetalleReporteDanio } from '../detalle-reporte-danio.model';
import { DetalleReporteDanioService } from '../service/detalle-reporte-danio.service';

@Component({
  standalone: true,
  templateUrl: './detalle-reporte-danio-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DetalleReporteDanioDeleteDialogComponent {
  detalleReporteDanio?: IDetalleReporteDanio;

  constructor(
    protected detalleReporteDanioService: DetalleReporteDanioService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.detalleReporteDanioService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
