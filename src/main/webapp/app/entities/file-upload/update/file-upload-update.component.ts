import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { FileUploadFormService, FileUploadFormGroup } from './file-upload-form.service';
import { IFileUpload } from '../file-upload.model';
import { FileUploadService } from '../service/file-upload.service';

@Component({
  standalone: true,
  selector: 'jhi-file-upload-update',
  templateUrl: './file-upload-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FileUploadUpdateComponent implements OnInit {
  isSaving = false;
  fileUpload: IFileUpload | null = null;

  editForm: FileUploadFormGroup = this.fileUploadFormService.createFileUploadFormGroup();

  constructor(
    protected fileUploadService: FileUploadService,
    protected fileUploadFormService: FileUploadFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileUpload }) => {
      this.fileUpload = fileUpload;
      if (fileUpload) {
        this.updateForm(fileUpload);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fileUpload = this.fileUploadFormService.getFileUpload(this.editForm);
    if (fileUpload.id !== null) {
      this.subscribeToSaveResponse(this.fileUploadService.update(fileUpload));
    } else {
      this.subscribeToSaveResponse(this.fileUploadService.create(fileUpload));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFileUpload>>): void {
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

  protected updateForm(fileUpload: IFileUpload): void {
    this.fileUpload = fileUpload;
    this.fileUploadFormService.resetForm(this.editForm, fileUpload);
  }
}
