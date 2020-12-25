import { TimeTracking } from "./TimeTracking";
import { WorkOrderTractorDriver } from "./WorkOrderTractorDriver";

export class WorkerTimeTracking {
    headerInfo: WorkOrderTractorDriver;
    times: TimeTracking[] = [];
}