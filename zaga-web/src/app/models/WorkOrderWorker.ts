import { Employee } from "./Employee";
import { Operation } from "./Operation";

export class WorkOrderWorker {
  id;
  user: Employee = new Employee();
  operation: Operation = new Operation();
  date;
  dayWorkPeriod;
  dayNightPeriod;
  workPeriod;
}
