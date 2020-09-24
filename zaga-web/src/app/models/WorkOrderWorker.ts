import { Employee } from "./Employee";
import { Operation } from "./Operation";

export class WorkOrderWorker {
  id;
  user: Employee;
  operation: Operation;
  date;
  dayWorkPeriod;
  dayNightPeriod;
  workPeriod;
}
