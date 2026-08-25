import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Tracker, TrackerRequest } from '../../../models/tracker.model';

@Component({
  selector: 'app-tracker-form',
  imports: [ReactiveFormsModule],
  templateUrl: './tracker-form.html',
  styleUrl: './tracker-form.css',
})
export class TrackerForm {

  private fb=inject(FormBuilder);


  @Input() trackerData:Tracker|null=null;
  @Input() isEditMode:boolean=false;


  @Output() save=new EventEmitter<TrackerRequest>();
  @Output() cancel=new EventEmitter<void>();

  trackerForm!:FormGroup;

  ngOnInit() {
    this.trackerForm=this.fb.group({
      trackerName:[this.trackerData?.trackerName|| '',Validators.required],
      description:[this.trackerData?.description|| '', Validators.required],
      active:[this.trackerData?.active|| true,Validators.required]
    })
  }   


  onSubmit(){
    if(this.trackerForm.valid){
      this.save.emit(this.trackerForm.value);
    }else{
      this.trackerForm.markAllAsTouched();
    }
  }

  onCancel(){
    this.cancel.emit();
  }

  getErrorMessage(control:string):string{
    const controlObj=this.trackerForm.get(control);
    if(controlObj?.hasError('required')){
      return 'This field is required';
    }else if(controlObj?.hasError('minlength')){
      return `${control} must be at least ${controlObj.errors?.['minlength'].requiredLength} characters long`;
    }
    return '';
  }
}
