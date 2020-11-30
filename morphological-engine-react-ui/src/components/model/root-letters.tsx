import {v4 as uuid} from 'uuid';
import { ArabicLetter } from './arabic-letter';
import { RootLetters as _RootLetters } from './conjugation-header';

export class RootLetters {
  private _id: string = uuid();

  constructor(public firstRadical: ArabicLetter = ArabicLetter.FA, public secondRadical: ArabicLetter = ArabicLetter.AIN,
    public thirdRadical: ArabicLetter = ArabicLetter.LAM, public fourthRadical: ArabicLetter = ArabicLetter.TATWEEL) { }

  get id(): string {
    return this._id;
  }

  set id(value: string) {
    this._id = value ? value : uuid();
  }

  get empty(): boolean {
    return this.firstRadical === null || this.secondRadical === null || this.thirdRadical === null;
  }

  get label(): string {
    let label = this.firstRadical.label + this.secondRadical.label + this.thirdRadical.label;
    if (this.hasFourthRadical) {
      label += this.fourthRadical.label;
    }
    return label;
  }

  get name(): string {
    let result = this.firstRadical.name + '_' + this.secondRadical.name + '_' + this.thirdRadical.name;
    if (this.hasFourthRadical) {
      result += '_' + this.fourthRadical.name;
    }
    return result;
  }

  get hasFourthRadical(): boolean {
    return this.fourthRadical !== null && this.fourthRadical.name !== ArabicLetter.TATWEEL.name;
  }

  public compareTo(other: RootLetters): number {
    let result = this.firstRadical.compareTo(other.firstRadical);
    if (result !== 0) {
      return result;
    }
    result = this.secondRadical.compareTo(other.secondRadical);
    if (result !== 0) {
      return result;
    }
    result = this.thirdRadical.compareTo(other.thirdRadical);
    if (result !== 0) {
      return result;
    }
    result = this.fourthRadical.compareTo(other.fourthRadical);
    if (result !== 0) {
      return result;
    }
    return result;
  }

  public equals(other: RootLetters): boolean {
    let result = (this.firstRadical.equals(other.firstRadical)) && (this.secondRadical.equals(other.secondRadical))
      && (this.thirdRadical.equals(other.thirdRadical));
    if (this.fourthRadical !== null) {
      result = result && (this.fourthRadical.equals(other.fourthRadical));
    }
    return result;
  }

  public copy(): RootLetters {
    return new RootLetters(this.firstRadical.copy(), this.secondRadical.copy(), this.thirdRadical, this.fourthRadical.copy());
  }

  public toRootLetters(): _RootLetters {
    return new _RootLetters(
      this.id,
      this.name,
      this.firstRadical.name,
      this.secondRadical.name,
      this.thirdRadical.name,
      this.fourthRadical.equals(ArabicLetter.TATWEEL) ? undefined : this.fourthRadical.name,
      this.name,
      this.empty
    );
  }
}