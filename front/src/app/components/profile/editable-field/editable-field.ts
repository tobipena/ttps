import { Component, Input, Output, EventEmitter, ViewEncapsulation, HostBinding } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-editable-field',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './editable-field.html',
  styleUrl: './editable-field.css',
  encapsulation: ViewEncapsulation.None
})
export class EditableField {
  @HostBinding('class') get hostClasses(): string {
    return 'col-md-6';
  }
  @Input() label: string = '';
  @Input() fieldName: string = '';
  @Input() value: any = '';
  @Input() type: string = 'text';
  @Input() placeholder: string = '';
  @Input() icon: string = '';
  @Input() isEditing: boolean = false;
  @Input() tempValue: string = '';
  @Input() confirmValue: string = '';
  @Input() showPassword: boolean = false;
  @Input() showConfirmPassword: boolean = false;

  @Output() startEdit = new EventEmitter<void>();
  @Output() save = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();
  @Output() togglePassword = new EventEmitter<void>();
  @Output() toggleConfirmPassword = new EventEmitter<void>();
  @Output() tempValueChange = new EventEmitter<string>();
  @Output() confirmValueChange = new EventEmitter<string>();

  onTempValueChange(value: string): void {
    this.tempValueChange.emit(value);
  }

  onConfirmValueChange(value: string): void {
    this.confirmValueChange.emit(value);
  }

  getDisplayValue(): string {
    if (this.type === 'password') {
      return '••••••••';
    }
    return this.value || 'No especificada';
  }
}
