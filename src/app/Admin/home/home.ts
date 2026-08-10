import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  trackers = [
    {
      name: 'java',
      count: 21,
    },
    {
      name: '.Net',
      count: 21,
    },
    {
      name: 'DevOps',
      count: 21,
    },
    {
      name: 'Cyber Security',
      count: 21,
    }
  ];
}
