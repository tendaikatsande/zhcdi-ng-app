import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { CsoFormService, CsoFormGroup } from './cso-form.service';
import { ICso } from '../cso.model';
import { CsoService } from '../service/cso.service';
import { IProvince } from 'app/entities/province/province.model';
import { ProvinceService } from 'app/entities/province/service/province.service';

@Component({
  standalone: true,
  selector: 'jhi-cso-update',
  templateUrl: './cso-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CsoUpdateComponent implements OnInit {
  isSaving = false;
  cso: ICso | null = null;

  provincesSharedCollection: IProvince[] = [];

  editForm: CsoFormGroup = this.csoFormService.createCsoFormGroup();

  constructor(
    protected csoService: CsoService,
    protected csoFormService: CsoFormService,
    protected provinceService: ProvinceService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProvince = (o1: IProvince | null, o2: IProvince | null): boolean => this.provinceService.compareProvince(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cso }) => {
      this.cso = cso;
      if (cso) {
        this.updateForm(cso);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cso = this.csoFormService.getCso(this.editForm);
    if (cso.id !== null) {
      this.subscribeToSaveResponse(this.csoService.update(cso));
    } else {
      this.subscribeToSaveResponse(this.csoService.create(cso));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICso>>): void {
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

  protected updateForm(cso: ICso): void {
    this.cso = cso;
    this.csoFormService.resetForm(this.editForm, cso);

    this.provincesSharedCollection = this.provinceService.addProvinceToCollectionIfMissing<IProvince>(
      this.provincesSharedCollection,
      ...(cso.provinces ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.provinceService
      .query()
      .pipe(map((res: HttpResponse<IProvince[]>) => res.body ?? []))
      .pipe(
        map((provinces: IProvince[]) =>
          this.provinceService.addProvinceToCollectionIfMissing<IProvince>(provinces, ...(this.cso?.provinces ?? []))
        )
      )
      .subscribe((provinces: IProvince[]) => (this.provincesSharedCollection = provinces));
  }
}
