export interface Task {
  id?: number;
  description: string;
  createdDate: string; // format YYYY-MM-DD
  dueDate: string;     // format YYYY-MM-DD
}
