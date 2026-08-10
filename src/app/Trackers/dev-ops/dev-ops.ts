import { Component } from '@angular/core';

@Component({
  selector: 'app-dev-ops',
  imports: [],
  templateUrl: './dev-ops.html',
  styleUrl: './dev-ops.css',
})
export class DevOps {candidates = [
    {
      candidateName: 'Ragul',
      email: 'ragul@gmail.com',
      degree: 'B.E',
      stream: 'CSD',
      year: 2026,
      computerTools: {
        mobile: 'Yes',
        internet: 'Yes',
        laptop: 'Yes',
      },
      english: {
        spoken: 'intermidiate',
        writing: 'intermidiate',
      },
    },
    {
      candidateName: 'Kumar',
      email: 'kumar@gmail.com',
      degree: 'B.Tech',
      stream: 'CSE',
      year: 2025,
      computerTools: {
        mobile: 'Yes',
        internet: 'No',
        laptop: 'Yes',
      },
      english: {
        spoken: 'intermidiate',
        writing: 'intermidiate',
      },
    },
  ];}
