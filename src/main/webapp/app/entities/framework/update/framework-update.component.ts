import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { FrameworkFormService, FrameworkFormGroup } from './framework-form.service';
import { IFramework } from '../framework.model';
import { FrameworkService } from '../service/framework.service';
import { IFrameworkType } from 'app/entities/framework-type/framework-type.model';
import { FrameworkTypeService } from 'app/entities/framework-type/service/framework-type.service';
import { IFileUpload } from 'app/entities/file-upload/file-upload.model';
import { FileUploadService } from 'app/entities/file-upload/service/file-upload.service';

@Component({
  standalone: true,
  selector: 'jhi-framework-update',
  templateUrl: './framework-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FrameworkUpdateComponent implements OnInit {
  isSaving = false;
  framework: IFramework | null = null;

  frameworkTypesSharedCollection: IFrameworkType[] = [];
  fileUploadsSharedCollection: IFileUpload[] = [];

  editForm: FrameworkFormGroup = this.frameworkFormService.createFrameworkFormGroup();

  constructor(
    protected frameworkService: FrameworkService,
    protected frameworkFormService: FrameworkFormService,
    protected frameworkTypeService: FrameworkTypeService,
    protected fileUploadService: FileUploadService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFrameworkType = (o1: IFrameworkType | null, o2: IFrameworkType | null): boolean =>
    this.frameworkTypeService.compareFrameworkType(o1, o2);

  compareFileUpload = (o1: IFileUpload | null, o2: IFileUpload | null): boolean => this.fileUploadService.compareFileUpload(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ framework }) => {
      this.framework = framework;
      if (framework) {
        this.updateForm(framework);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const framework = this.frameworkFormService.getFramework(this.editForm);
    if (framework.id !== null) {
      this.subscribeToSaveResponse(this.frameworkService.update(framework));
    } else {
      this.subscribeToSaveResponse(this.frameworkService.create(framework));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFramework>>): void {
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

  protected updateForm(framework: IFramework): void {
    this.framework = framework;
    this.frameworkFormService.resetForm(this.editForm, framework);

    this.frameworkTypesSharedCollection = this.frameworkTypeService.addFrameworkTypeToCollectionIfMissing<IFrameworkType>(
      this.frameworkTypesSharedCollection,
      ...(framework.frameworkTypes ?? [])
    );
    this.fileUploadsSharedCollection = this.fileUploadService.addFileUploadToCollectionIfMissing<IFileUpload>(
      this.fileUploadsSharedCollection,
      ...(framework.fileUploads ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.frameworkTypeService
      .query()
      .pipe(map((res: HttpResponse<IFrameworkType[]>) => res.body ?? []))
      .pipe(
        map((frameworkTypes: IFrameworkType[]) =>
          this.frameworkTypeService.addFrameworkTypeToCollectionIfMissing<IFrameworkType>(
            frameworkTypes,
            ...(this.framework?.frameworkTypes ?? [])
          )
        )
      )
      .subscribe((frameworkTypes: IFrameworkType[]) => (this.frameworkTypesSharedCollection = frameworkTypes));

    this.fileUploadService
      .query()
      .pipe(map((res: HttpResponse<IFileUpload[]>) => res.body ?? []))
      .pipe(
        map((fileUploads: IFileUpload[]) =>
          this.fileUploadService.addFileUploadToCollectionIfMissing<IFileUpload>(fileUploads, ...(this.framework?.fileUploads ?? []))
        )
      )
      .subscribe((fileUploads: IFileUpload[]) => (this.fileUploadsSharedCollection = fileUploads));
  }
}
