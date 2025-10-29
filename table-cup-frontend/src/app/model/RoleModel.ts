
export class RoleModel {
  internalName: string
  displayName: string


  constructor(internalName: string, displayName: string) {
    this.internalName = internalName;
    this.displayName = displayName;
  }
}
