import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRegistroDanio } from '../registro-danio.model';
import { RegistroDanioService } from '../service/registro-danio.service';

@Component({
  standalone: true,
  templateUrl: './registro-danio-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RegistroDanioDeleteDialogComponent {
  registroDanio?: IRegistroDanio;

  constructor(
    protected registroDanioService: RegistroDanioService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.registroDanioService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
