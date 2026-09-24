import {Component, EventEmitter, Input, Output} from '@angular/core';
import {TaskChildComponent} from "./task-child/task-child.component";

@Component({
  selector: 'app-task',
  standalone: true,
  imports: [
    TaskChildComponent
  ],
  templateUrl: './task.component.html',
  styleUrl: './task.component.css'
})
export class TaskComponent {

  @Input({required: true}) name!: string;

}
