import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IParametroSistema } from '../parametro-sistema.model';
import { ParametroSistemaService } from '../service/parametro-sistema.service';

@Component({
  standalone: true,
  templateUrl: './parametro-sistema-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ParametroSistemaDeleteDialogComponent {
  parametroSistema?: IParametroSistema;

  constructor(
    protected parametroSistemaService: ParametroSistemaService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.parametroSistemaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
