export class ArabicLabel {
    constructor(public name: string, public label: string, public code: string) { }

    public equals(other?: ArabicLabel): boolean {
        return other ? other.code === this.code : false;
    }

    public compareTo(other: ArabicLabel): number {
        return this.label.localeCompare(other.label);
    }
}
