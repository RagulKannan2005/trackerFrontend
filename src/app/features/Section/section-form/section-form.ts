import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Section, SectionRequest } from '../../../models/section.model';

@Component({
  selector: 'app-section-form',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './section-form.html',
  styleUrl: './section-form.css',
})
export class SectionForm {
  private fb = inject(FormBuilder);

  @Input() sectionData: Section | null = null;
  @Input() isEditMode: boolean = false;

  @Output() save = new EventEmitter<SectionRequest>();
  @Output() cancel = new EventEmitter<void>();

  sectionForm!: FormGroup;

  ngOnInit() {
    this.sectionForm = this.fb.group({
      sectionName: [this.sectionData?.sectionName || '', Validators.required],
      description: [this.sectionData?.description || '', Validators.required],
      active: [this.sectionData?.active || true],
    });
  }

  onSubmit() {
    if (this.sectionForm.valid) {
      const section: SectionRequest = this.sectionForm.value;
      this.save.emit(section);
      this.sectionForm.reset();
    } else {
      this.sectionForm.markAllAsTouched();
      console.log('Form invalid');
    }
  }

  onCancel() {
    this.cancel.emit();
    this.sectionForm.reset();
  }

  getErrorMessage(controlName: string) {
    const control = this.sectionForm.get(controlName);
    if (control?.hasError('required')) {
      return 'This field is required';
    }
    if (control?.hasError('minlength')) {
      return 'Minimum length is ' + control.getError('minlength').requiredLength;
    }
    if (control?.hasError('maxlength')) {
      return 'Maximum length is ' + control.getError('maxlength').requiredLength;
    }
    return '';
  }
}
