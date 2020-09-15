import { Material } from './Material';
import { Machine } from './Machine';
import { Employee } from './Employee';

export class WorkOrder{
    id:string;
    start;
    end;
    status:string;
    cropId:string;
    operationId:string;
    responsibleId:string;
    materials:Material [] = [];
    machines:Machine [] = [];
    workers: Employee [] = [];
}