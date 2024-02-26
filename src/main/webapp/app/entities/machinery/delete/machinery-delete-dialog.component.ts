import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMachinery } from '../machinery.model';
import { MachineryService } from '../service/machinery.service';

@Component({
  standalone: true,
  templateUrl: './machinery-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MachineryDeleteDialogComponent {
  machinery?: IMachinery;

  constructor(
    protected machineryService: MachineryService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.machineryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
