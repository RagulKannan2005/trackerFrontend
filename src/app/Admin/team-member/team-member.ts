import { NgIf } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-team-member',
  imports: [NgIf],
  templateUrl: './team-member.html',
  styleUrl: './team-member.css',
})
export class TeamMember {

  showform:boolean=false;
}
