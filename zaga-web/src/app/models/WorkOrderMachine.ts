import { Employee } from "./Employee";
import { Machine } from "./Machine";

export class WorkOrderMachine {
  id;
  user: Employee = new Employee();
  machine: Machine = new Machine();
  initialState;
  finalState;
  sumState;
  workPeriod;
  date;
  fuel;
}
