export class ArabicLabel {
    constructor(public name: string, public label: string, public code: string) { }
  
    equals(other: ArabicLabel): boolean {
      return (other !== null) && (other.name === this.name);
    }
  }