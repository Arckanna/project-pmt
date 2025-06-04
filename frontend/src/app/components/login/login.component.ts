import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  email = '';
  password = '';
  message = '';
  success = false;

  constructor(private userService: UserService, private router: Router)
  {

  }

  onLogin() {
    this.userService.login(this.email, this.password).subscribe({
      next: (res) => {
        this.message = res;
        this.success = true;
        this.router.navigate(['/home']);
      },
      error: (err) => {
        this.message = 'Identifiants incorrects';
        this.success = false;
      }
    });
  }

}
