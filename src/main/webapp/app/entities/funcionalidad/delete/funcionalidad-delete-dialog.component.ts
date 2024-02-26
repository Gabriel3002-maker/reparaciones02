import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFuncionalidad } from '../funcionalidad.model';
import { FuncionalidadService } from '../service/funcionalidad.service';

@Component({
  standalone: true,
  templateUrl: './funcionalidad-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FuncionalidadDeleteDialogComponent {
  funcionalidad?: IFuncionalidad;

  constructor(
    protected funcionalidadService: FuncionalidadService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.funcionalidadService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
