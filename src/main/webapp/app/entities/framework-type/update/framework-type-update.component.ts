import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { FrameworkTypeFormService, FrameworkTypeFormGroup } from './framework-type-form.service';
import { IFrameworkType } from '../framework-type.model';
import { FrameworkTypeService } from '../service/framework-type.service';

@Component({
  standalone: true,
  selector: 'jhi-framework-type-update',
  templateUrl: './framework-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FrameworkTypeUpdateComponent implements OnInit {
  isSaving = false;
  frameworkType: IFrameworkType | null = null;

  editForm: FrameworkTypeFormGroup = this.frameworkTypeFormService.createFrameworkTypeFormGroup();

  constructor(
    protected frameworkTypeService: FrameworkTypeService,
    protected frameworkTypeFormService: FrameworkTypeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ frameworkType }) => {
      this.frameworkType = frameworkType;
      if (frameworkType) {
        this.updateForm(frameworkType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const frameworkType = this.frameworkTypeFormService.getFrameworkType(this.editForm);
    if (frameworkType.id !== null) {
      this.subscribeToSaveResponse(this.frameworkTypeService.update(frameworkType));
    } else {
      this.subscribeToSaveResponse(this.frameworkTypeService.create(frameworkType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFrameworkType>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(frameworkType: IFrameworkType): void {
    this.frameworkType = frameworkType;
    this.frameworkTypeFormService.resetForm(this.editForm, frameworkType);
  }
}
