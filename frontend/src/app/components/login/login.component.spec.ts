import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let userServiceSpy: jasmine.SpyObj<UserService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    userServiceSpy = jasmine.createSpyObj('UserService', ['login']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [FormsModule, HttpClientTestingModule],
      providers: [
        { provide: Router, useValue: routerSpy },
        { provide: UserService, useValue: userServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('devrait créer le composant', () => {
    expect(component).toBeTruthy();
  });

  it('devrait appeler login() avec email et mot de passe', () => {
    component.email = 'test@example.com';
    component.password = 'azerty';
    userServiceSpy.login.and.returnValue(of('Connexion réussie'));

    component.onLogin();

    expect(userServiceSpy.login).toHaveBeenCalledWith('test@example.com', 'azerty');
    expect(component.success).toBeTrue();
    expect(component.message).toBe('Connexion réussie');
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/home']);
  });

  it('devrait afficher un message d\'erreur si les identifiants sont incorrects', () => {
    userServiceSpy.login.and.returnValue(throwError(() => new Error('401')));

    component.email = 'wrong@example.com';
    component.password = 'wrong';
    component.onLogin();

    expect(userServiceSpy.login).toHaveBeenCalled();
    expect(component.success).toBeFalse();
    expect(component.message).toBe('Identifiants incorrects');
  });
});
