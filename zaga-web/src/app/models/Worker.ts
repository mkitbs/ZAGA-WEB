export class Worker {
    id;
    worker;
    date;
    dayWorkPeriod;
    nightWorkPeriod;
    workPeriod: number = this.nightWorkPeriod + this.dayWorkPeriod;
    operation;
    responsible;

}