import { Component, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { skillsService } from '../../../services/skill.service';
import { Auth } from '../../../services/auth';
import { Router } from '@angular/router';
import { Skill, SkillsRequest } from '../../../models/skill.model';
import { SkillForm } from '../skill-form/skill-form';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-skill-list',
  standalone: true,
  imports: [CommonModule, SkillForm],
  templateUrl: './skill-list.html',
  styleUrl: './skill-list.css',
})
export class SkillList {
  private skillservice = inject(skillsService);
  private authService = inject(Auth);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  skills: Skill[] = [];
  filteredSkills: Skill[] = [];
  isLoading: boolean = false;
  errorMessage: string = '';

  isModelOpen: boolean = false;
  isEditMode: boolean = false;
  selectedSkill: Skill | null = null;

  ngOnInit() {
    this.loadSkills();
  }

  isAdmin(): boolean {
    const role = this.authService.getrole();
    if (!role) return true;
    const r = String(role).toUpperCase();
    return r === 'ADMIN' || r === 'ROLE_ADMIN' || r.includes('ADMIN');
  }
  currentFilters={searchTerm:'',statusFilter:'ALL'};

  loadSkills() {
    this.isLoading = true;
    this.errorMessage = '';
    this.cdr.markForCheck();

    this.skillservice
      .getAllSkills()
      .pipe(
        finalize(() => {
          this.isLoading = false;
          this.cdr.markForCheck();
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: (data) => {
          this.skills = data || [];
          this.applyFilters();
          this.cdr.markForCheck();
        },
        error: (err) => {
          this.errorMessage = err?.error?.message || err?.message || 'Failed to load skills from server';
          this.cdr.markForCheck();
        },
      });
  }

  onFilterChange(filters: { searchTerm: string; statusFilter: string }) {
    this.currentFilters = filters || { searchTerm: '', statusFilter: 'ALL' };
    this.applyFilters();
  }

  applyFilters() {
    let result = [...(this.skills || [])];
    if (this.currentFilters?.searchTerm?.trim()) {
      const term = this.currentFilters.searchTerm.toLowerCase().trim();
      result = result.filter((s) => (s?.skillName || '').toLowerCase().includes(term));
    }
    if (this.currentFilters?.statusFilter === 'ACTIVE') {
      result = result.filter((s) => Boolean(s?.active) === true);
    } else if (this.currentFilters?.statusFilter === 'INACTIVE') {
      result = result.filter((s) => Boolean(s?.active) === false);
    }
    this.filteredSkills = result;
    this.cdr.markForCheck();
  }

  openAddModel(){
    this.selectedSkill=null;
    this.isEditMode=false;
    this.isModelOpen=true;
  }
  closeModel(){
    this.isModelOpen=false;
    this.selectedSkill=null;
  }

  handleFormSave(formData: SkillsRequest){
    if(this.isEditMode && this.selectedSkill?.id){
      this.skillservice.updateSkill(this.selectedSkill.id,formData).subscribe({
        next:()=>{
          this.closeModel();
          this.loadSkills();
        },
        error:()=>{
          alert('Failed to update skill');
        }
      })
    }
    else{
      this.skillservice.addSkill(formData).subscribe({
        next:()=>{
          this.closeModel();
          this.loadSkills();
        },
        error:()=>{
          alert('Failed to create skill');
        }
      })
    }
  }
  openEditModel(skill:Skill){
    this.selectedSkill=skill;
    this.isEditMode=true;
    this.isModelOpen=true;
  }
  deleteSkill(id:number){
    if(confirm('Are you sure you want to delete this skill?')){
      this.skillservice.deleteSkill(id).subscribe({
        next:()=>{
          this.loadSkills();
        },
        error:()=>{
          alert('Failed to delete skill');
        }
      })
    }
  }
  viewDetails(id:number){
    this.router.navigate(['/admin/skills',id]);
  }
  get activeCount(){
    return this.skills.filter(s=>s?.active).length;
  }
  get inactiveCount(){
    return this.skills.filter(s=>!s?.active).length;
  }
}
