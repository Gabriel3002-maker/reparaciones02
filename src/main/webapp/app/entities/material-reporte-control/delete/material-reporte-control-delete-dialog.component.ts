import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMaterialReporteControl } from '../material-reporte-control.model';
import { MaterialReporteControlService } from '../service/material-reporte-control.service';

@Component({
  standalone: true,
  templateUrl: './material-reporte-control-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MaterialReporteControlDeleteDialogComponent {
  materialReporteControl?: IMaterialReporteControl;

  constructor(
    protected materialReporteControlService: MaterialReporteControlService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.materialReporteControlService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
