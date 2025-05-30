export interface Task {
  id?: number;
  description: string;
  createdDate: string;
  dueDate: string;
  assignedTo: { email: string } ;
}
