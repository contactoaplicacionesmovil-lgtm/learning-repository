import {Component, Input, input, computed} from '@angular/core';


@Component({
  selector: 'app-user',
  standalone: true,
  imports: [],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css'
})
export class UserComponent {
  //SIGNAL APPROUCH
  //wich kind of value will eventually be received
  //avatar = input.required<string>();
  //name = input.required<string>();
  // imagePath = computed(() => {
  //   return 'assets/users/' + this.avatar();
  // });

  @Input({required: true}) avatar!: string;
  @Input({required: true}) name!: string;

  get imagePath(){
    return 'assets/users/' + this.avatar;
  }

  onSelectUser() {

  }

}
