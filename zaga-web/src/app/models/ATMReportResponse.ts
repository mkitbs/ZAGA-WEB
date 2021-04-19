import { ATMReport } from "./ATMReport";

export class ATMReportResponse {
    atm;
    workOrders: ATMReport[] = [];
}