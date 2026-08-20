import { Component, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SectionService } from '../../../services/section.service';
import { Section } from '../../../models/section.model';
import { SectionSkillService } from '../../../services/section-skill.service';
import { SectionSkillResponse } from '../../../models/section-skill.model';
import { skillsService } from '../../../services/skill.service';
import { Skill } from '../../../models/skill.model';
import { Auth } from '../../../services/auth';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-section-deatils',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './section-deatils.html',
  styleUrl: './section-deatils.css',
})
export class SectionDeatils {
  private route = inject(ActivatedRoute);
  private sectionService = inject(SectionService);
  private sectionSkillService = inject(SectionSkillService);
  private skillService = inject(skillsService);
  private router = inject(Router);
  public authService = inject(Auth);
  private cdr = inject(ChangeDetectorRef);

  sectionId: number | null = null;
  section: Section | null = null;
  assignedSkills: SectionSkillResponse[] = [];
  availableSkills: Skill[] = [];
  isLoading: boolean = true;
  errorMessage: string = '';

  // Modal State for Assigning Skill
  isAssignModalOpen: boolean = false;
  selectedSkillId: number | null = null;
  displayOrder: number = 1;
  isSubmitting: boolean = false;

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      const idparam = params.get('id');
      if (idparam) {
        this.sectionId = Number(idparam);
        this.loadSection(this.sectionId);
        this.loadAssignedSkills();
      } else {
        this.isLoading = false;
        this.errorMessage = 'No section ID provided.';
        this.cdr.markForCheck();
      }
    });
  }

  isAdmin(): boolean {
    const role = this.authService.getrole();
    if (!role) return true;
    const r = String(role).toUpperCase();
    return r === 'ADMIN' || r === 'ROLE_ADMIN' || r.includes('ADMIN');
  }

  loadSection(id: number) {
    this.isLoading = true;
    this.sectionService
      .getSectionById(id)
      .pipe(
        finalize(() => {
          this.isLoading = false;
          this.cdr.markForCheck();
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: (data) => {
          this.section = data;
          this.cdr.markForCheck();
        },
        error: (err) => {
          console.error('Failed to load section:', err);
          this.errorMessage = 'Failed to load section details.';
          this.cdr.markForCheck();
        },
      });
  }

  loadAssignedSkills() {
    this.sectionSkillService.getAllSectionSkills().subscribe({
      next: (skills: SectionSkillResponse[]) => {
        // Filter section skills belonging to current section ID
        this.assignedSkills = (skills || [])
          .filter((ss: SectionSkillResponse) => Number(ss.trackerSectionId) === Number(this.sectionId))
          .sort((a: SectionSkillResponse, b: SectionSkillResponse) => (a.displayOrder || 0) - (b.displayOrder || 0));
        this.cdr.markForCheck();
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('Failed to load assigned skills:', err),
    });
  }

  openAssignSkillModal() {
    this.selectedSkillId = null;
    this.displayOrder = this.assignedSkills.length + 1;
    this.isAssignModalOpen = true;

    if (this.availableSkills.length === 0) {
      this.skillService.getAllSkills().subscribe({
        next: (skills: Skill[]) => {
          this.availableSkills = skills || [];
          this.cdr.markForCheck();
        },
        error: (err: any) => console.error('Failed to load available skills:', err),
      });
    }
  }

  closeAssignModal() {
    this.isAssignModalOpen = false;
    this.selectedSkillId = null;
  }

  saveAssignSkill() {
    if (!this.selectedSkillId || !this.sectionId) {
      alert('Please select a skill');
      return;
    }

    this.isSubmitting = true;
    const payload = {
      trackerSectionId: this.sectionId,
      skillId: Number(this.selectedSkillId),
      displayOrder: Number(this.displayOrder),
    };

    this.sectionSkillService.assignSkillToSection(payload).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.closeAssignModal();
        this.loadAssignedSkills();
      },
      error: (err: any) => {
        this.isSubmitting = false;
        console.error('Failed to assign skill:', err);
        alert(err?.error?.message || 'Failed to assign skill to section.');
      },
    });
  }

  removeSkill(sectionSkillId: number) {
    if (confirm('Are you sure you want to remove this skill from the section?')) {
      this.sectionSkillService.deleteSectionSkill(sectionSkillId).subscribe({
        next: () => {
          this.loadAssignedSkills();
        },
        error: (err: any) => {
          console.error('Failed to remove skill:', err);
          alert('Failed to remove skill from section.');
        },
      });
    }
  }

  goBack() {
    this.router.navigateByUrl('/admin/sections');
  }
}

