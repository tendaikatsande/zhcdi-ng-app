import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IFrameworkType } from '../framework-type.model';
import { FrameworkTypeService } from '../service/framework-type.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './framework-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FrameworkTypeDeleteDialogComponent {
  frameworkType?: IFrameworkType;

  constructor(protected frameworkTypeService: FrameworkTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.frameworkTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
