import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMaterialDanio } from '../material-danio.model';
import { MaterialDanioService } from '../service/material-danio.service';

@Component({
  standalone: true,
  templateUrl: './material-danio-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MaterialDanioDeleteDialogComponent {
  materialDanio?: IMaterialDanio;

  constructor(
    protected materialDanioService: MaterialDanioService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.materialDanioService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
