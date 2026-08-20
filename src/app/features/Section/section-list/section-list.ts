import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { SectionForm } from '../section-form/section-form';
import { SectionFilter } from '../section-filter/section-filter';
import { SectionService } from '../../../services/section.service';
import { Auth } from '../../../services/auth';
import { Router } from '@angular/router';
import { Section, SectionRequest } from '../../../models/section.model';

@Component({
  selector: 'app-section-list',
  standalone: true,
  imports: [SectionForm, SectionFilter],
  templateUrl: './section-list.html',
  styleUrl: './section-list.css',
})
export class SectionList implements OnInit {
  private sectionservice = inject(SectionService);
  public authService = inject(Auth);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  section: Section[] = [];
  filteredSection: Section[] = [];
  isLoading: boolean = false;
  errorMessage: string = '';

  isModelOpen: boolean = false;
  isEditMode: boolean = false;
  selectedSection: Section | null = null;

  ngOnInit() {
    this.loadSections();
  }

  isAdmin(): boolean {
    const role = this.authService.getrole();
    if (!role) return true;
    const r = String(role).toUpperCase();
    return r === 'ADMIN' || r === 'ROLE_ADMIN' || r.includes('ADMIN');
  }

  currentFilters = { searchTerm: '', statusFilter: 'ALL' };

  loadSections() {
    this.isLoading = true;
    this.errorMessage = '';
    this.cdr.markForCheck();

    this.sectionservice.getAllSections().subscribe({
      next: (data) => {
        this.section = Array.isArray(data) ? data : (data as any)?.content || [];
        this.applyFilters();
        this.isLoading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || err?.message || 'Failed to load sections from server';
        this.isLoading = false;
        this.cdr.markForCheck();
        console.error(err);
      },
    });
  }

  onFilterChange(filters: { searchTerm: string; statusFilter: string }) {
    this.currentFilters = filters || { searchTerm: '', statusFilter: 'ALL' };
    this.applyFilters();
  }

  applyFilters() {
    let result = [...(this.section || [])];

    if (this.currentFilters?.searchTerm?.trim()) {
      const term = this.currentFilters.searchTerm.toLowerCase().trim();
      result = result.filter(
        (s) =>
          (s?.sectionName ? String(s.sectionName).toLowerCase() : '').includes(term) ||
          (s?.description ? String(s.description).toLowerCase() : '').includes(term)
      );
    }

    if (this.currentFilters?.statusFilter === 'ACTIVE') {
      result = result.filter((s) => Boolean(s?.active) === true);
    } else if (this.currentFilters?.statusFilter === 'INACTIVE') {
      result = result.filter((s) => Boolean(s?.active) === false);
    }

    this.filteredSection = result;
  }

  openAddModel(){
    this.selectedSection=null;
    this.isEditMode=false;
    this.isModelOpen=true;
  }

  openEditModel(section:Section){
    this.selectedSection=section;
    this.isEditMode=true;
    this.isModelOpen=true;
  }

  closeModel(){
    this.isModelOpen=false;
    this.isEditMode=false;
    this.selectedSection=null;
  }

  handleFormsave(formdata:SectionRequest){
    if(this.isEditMode&&this.selectedSection?.id){
      this.sectionservice.updateSection(this.selectedSection.id,formdata).subscribe({
        next:()=>{
          this.closeModel();
          this.loadSections();
        },
        error:(err)=>{
          alert('Failed to update section.');
        }
      });

    }else{
      this.sectionservice.addSection(formdata).subscribe({
        next:()=>{
          this.closeModel();
          this.loadSections();
        },
        error:(err)=>alert('Failed to create section.'),
      });
    }
  }
  deleteSection(id:number){
    if(confirm('Are you sure you want to delete this section?')){
      this.sectionservice.deleteSection(id).subscribe({
        next:()=>this.loadSections(),
        error:()=>alert('Failed to delete section.'),
      });
    }
  }

  viewDetails(id:number){
    this.router.navigate(['/admin/sections',id]);
  }

  get activeCount():number{
    return (this.section || []).filter((s)=>Boolean(s?.active)).length;
  }

  get inactiveCount():number{
    return (this.section || []).filter((s)=>!Boolean(s?.active)).length;
  }

}
