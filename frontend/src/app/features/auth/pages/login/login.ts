import { Component, inject, signal } from '@angular/core';
import { AbstractControl, NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

@Component({
  imports: [MatButtonModule, MatFormFieldModule, MatIconModule, MatInputModule, ReactiveFormsModule, RouterLink],
  selector: 'app-login',
  styleUrl: './login.scss',
  templateUrl: './login.html',
})
export class Login {
  private readonly formBuilder = inject(NonNullableFormBuilder);
  protected readonly passwordVisible = signal(false);
  protected readonly loginForm = this.formBuilder.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
  });

  protected togglePassword(): void {
    this.passwordVisible.update((visible) => !visible);
  }

  protected onSubmit(): void {
    this.loginForm.markAllAsTouched();
    if (this.loginForm.invalid) return;
  }

  protected errorMessage(control: AbstractControl): string {
    if (control.hasError('required')) return 'Este campo é obrigatório.';
    if (control.hasError('email')) return 'Digite um e-mail válido.';
    if (control.hasError('minlength')) return 'A senha deve ter pelo menos 8 caracteres.';
    return '';
  }
}
