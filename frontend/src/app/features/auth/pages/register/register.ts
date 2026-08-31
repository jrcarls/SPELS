import { Component, signal } from '@angular/core';
import { email, form, FormField, minLength, required } from '@angular/forms/signals';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

@Component({
  imports: [FormField, MatButtonModule, MatCheckboxModule, MatFormFieldModule, MatIconModule, MatInputModule, RouterLink],
  selector: 'app-register',
  styleUrl: './register.scss',
  templateUrl: './register.html',
})
export class Register {
  protected readonly passwordVisible = signal(false);
  protected readonly registerModel = signal({ name: '', email: '', password: '', terms: false });
  protected readonly registerForm = form(this.registerModel, (path) => {
    required(path.name, { message: 'Informe o nome da sua confeitaria.' });
    required(path.email, { message: 'Informe seu e-mail.' });
    email(path.email, { message: 'Digite um e-mail válido.' });
    required(path.password, { message: 'Informe sua senha.' });
    minLength(path.password, 6, { message: 'A senha deve ter pelo menos 6 caracteres.' });
    required(path.terms, { message: 'Aceite os termos para continuar.' });
  });

  protected togglePassword(): void {
    this.passwordVisible.update((visible) => !visible);
  }

  protected onSubmit(): void {
    if (this.registerForm().invalid()) return;
  }
}
