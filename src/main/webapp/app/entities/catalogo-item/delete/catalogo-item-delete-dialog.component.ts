import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICatalogoItem } from '../catalogo-item.model';
import { CatalogoItemService } from '../service/catalogo-item.service';

@Component({
  standalone: true,
  templateUrl: './catalogo-item-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CatalogoItemDeleteDialogComponent {
  catalogoItem?: ICatalogoItem;

  constructor(
    protected catalogoItemService: CatalogoItemService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.catalogoItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
