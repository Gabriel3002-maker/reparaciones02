import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRolFuncionalidad } from '../rol-funcionalidad.model';
import { RolFuncionalidadService } from '../service/rol-funcionalidad.service';

@Component({
  standalone: true,
  templateUrl: './rol-funcionalidad-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RolFuncionalidadDeleteDialogComponent {
  rolFuncionalidad?: IRolFuncionalidad;

  constructor(
    protected rolFuncionalidadService: RolFuncionalidadService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rolFuncionalidadService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
