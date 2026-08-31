import { AbstractControl, NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { AuthApi } from '../../data-access/auth-api';

@Component({
  imports: [MatButtonModule, MatCheckboxModule, MatFormFieldModule, MatIconModule, MatInputModule, ReactiveFormsModule, RouterLink],
  selector: 'app-register',
  styleUrl: './register.scss',
  templateUrl: './register.html',
})
export class Register {
  private readonly formBuilder = inject(NonNullableFormBuilder);
  private readonly authApi = inject(AuthApi);
  protected readonly passwordVisible = signal(false);
  protected readonly submitting = signal(false);
  protected readonly submitError = signal('');
  protected readonly submitted = signal(false);
  protected readonly registerForm = this.formBuilder.group({
    name: ['', Validators.required],
    organizationName: ['', Validators.required],
    cnpj: ['', [Validators.maxLength(18), Validators.pattern(/^(?:[A-Za-z0-9]{14}|[A-Za-z0-9./-]{18})?$/)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
    terms: [false, Validators.requiredTrue],
  });

  protected togglePassword(): void {
    this.passwordVisible.update((visible) => !visible);
  }

  protected onSubmit(): void {
    this.registerForm.markAllAsTouched();
    if (this.registerForm.invalid) return;

    const { name, organizationName, cnpj, email, password } = this.registerForm.getRawValue();
    this.submitting.set(true);
    this.submitError.set('');

    this.authApi.register({
      name,
      organizationName,
      cnpj: cnpj || null,
      email,
      password,
    }).subscribe({
      next: () => {
        this.submitting.set(false);
        this.submitted.set(true);
      },
      error: (error: HttpErrorResponse) => {
        this.submitting.set(false);
        this.submitError.set(error.error?.message || 'Não foi possível criar sua conta. Tente novamente.');
      },
    });
  }

  protected errorMessage(control: AbstractControl): string {
    if (control.hasError('required')) return 'Este campo é obrigatório.';
    if (control.hasError('requiredtrue')) return 'Aceite os termos para continuar.';
    if (control.hasError('email')) return 'Digite um e-mail válido.';
    if (control.hasError('minlength')) return 'A senha deve ter pelo menos 8 caracteres.';
    if (control.hasError('maxlength')) return 'O CNPJ deve ter no máximo 18 caracteres.';
    if (control.hasError('pattern')) return 'Informe um CNPJ válido.';
    return '';
  }
}
