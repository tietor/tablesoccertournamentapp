export class StatusModel {
  internalStatus: string;
  displayStatus: string;

  constructor(internalStatus: string, displayStatus: string) {
    this.internalStatus = internalStatus;
    this.displayStatus = displayStatus;
  }
}
