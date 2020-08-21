export class WorkOrdeMachine {
    id;
    machine;
    worker;
    date;
    workPeriod;
    fuel;
    initialState;
    finalState;
    sumState = this.finalState - this.initialState;
}