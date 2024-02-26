import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOrdenBodega } from '../orden-bodega.model';
import { OrdenBodegaService } from '../service/orden-bodega.service';

@Component({
  standalone: true,
  templateUrl: './orden-bodega-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OrdenBodegaDeleteDialogComponent {
  ordenBodega?: IOrdenBodega;

  constructor(
    protected ordenBodegaService: OrdenBodegaService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ordenBodegaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
