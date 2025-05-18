import { Component } from '@angular/core';
import { User } from '../../models/user.model';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
})
export class UserFormComponent {
  user: User = {
    email: '',
    username: '',
    password: ''
  };

  success = false;

  constructor(private userService: UserService) {}

  onSubmit() {
    this.userService.createUser(this.user).subscribe({
      next: () => {
        this.success = true;
        this.user = { email: '', username: '', password: '' };
      },
      error: (err) => console.error('Erreur création utilisateur', err)
    });
  }
}
