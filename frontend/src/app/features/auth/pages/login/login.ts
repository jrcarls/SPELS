import { Component, signal } from '@angular/core';
import { email, form, FormField, minLength, required } from '@angular/forms/signals';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

@Component({
  imports: [FormField, MatButtonModule, MatFormFieldModule, MatIconModule, MatInputModule, RouterLink],
  selector: 'app-login',
  styleUrl: './login.scss',
  templateUrl: './login.html',
})
export class Login {
  protected readonly passwordVisible = signal(false);
  protected readonly loginModel = signal({ email: '', password: '' });
  protected readonly loginForm = form(this.loginModel, (path) => {
    required(path.email, { message: 'Informe seu e-mail.' });
    email(path.email, { message: 'Digite um e-mail válido.' });
    required(path.password, { message: 'Informe sua senha.' });
    minLength(path.password, 6, { message: 'A senha deve ter pelo menos 6 caracteres.' });
  });

  protected togglePassword(): void {
    this.passwordVisible.update((visible) => !visible);
  }

  protected onSubmit(): void {
    if (this.loginForm().invalid()) return;
  }
}
