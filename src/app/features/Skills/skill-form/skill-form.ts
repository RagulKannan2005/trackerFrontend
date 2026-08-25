import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Skill, SkillsRequest } from '../../../models/skill.model';
import { Component, EventEmitter, inject, Input, Output } from '@angular/core';

@Component({
  selector: 'app-skill-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './skill-form.html',
  styleUrl: './skill-form.css',
})
export class SkillForm {
  private fb = inject(FormBuilder);

  @Input() skilldata: Skill | null = null;
  @Input() isEditMode: boolean = false;

  @Output() formsubmit = new EventEmitter<SkillsRequest>();
  @Output() formcancel = new EventEmitter<void>();

  skillForm!: FormGroup;

  ngOnInit() {
    this.skillForm = this.fb.group({
      skillName: [this.skilldata?.skillName || '', Validators.required],
      description: [this.skilldata?.description || '', Validators.required],
      active: [this.skilldata?.active || true],
    });
  }

  onSubmit() {
    if (this.skillForm.valid) {
      const skill: SkillsRequest = this.skillForm.value;
      this.formsubmit.emit(skill);
      this.skillForm.reset();
    } else {
      this.skillForm.markAllAsTouched();
      console.log('Form Invalid');
    }
  }

  onCancel() {
    this.formcancel.emit();
    this.skillForm.reset();
  }

  etErrorMessage(controlName: string) {
    const control = this.skillForm.get(controlName);
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
