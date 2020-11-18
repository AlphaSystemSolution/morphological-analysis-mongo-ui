import { IdGenerator } from '../../utils/id-generator'
import { ArabicLetter } from './arabic-letter';
import { RootLetters as _RootLetters } from './root-letters';

export class RootLetters {
    constructor(
        public id: string = IdGenerator.nextId(),
        public name: string,
        public firstRadical: string,
        public secondRadical: string,
        public thirdRadical: string,
        public fourthRadical?: string,
        public displayName?: string,
        public empty?: string
    ) { }

    get label(): string {
        return this.firstRadical + this.secondRadical + this.thirdRadical;
    }

    public toRootLetters(): _RootLetters {
        return new _RootLetters(
            ArabicLetter.getByName(this.firstRadical),
            ArabicLetter.getByName(this.secondRadical),
            ArabicLetter.getByName(this.thirdRadical),
            ArabicLetter.getByName(this.fourthRadical ? this.fourthRadical : ArabicLetter.TATWEEL.name)
        );
    }

    static of(src: _RootLetters): RootLetters {
        return new RootLetters(
            IdGenerator.nextId(),
            src.name,
            src.firstRadical.name,
            src.secondRadical.name,
            src.thirdRadical.name,
            src.fourthRadical.name
        );
    }
}

export class ChartMode {
    constructor(public template: string, public rootType: string, public verbType: string, public weakVerbType: string) { }
}

export class ConjugationHeader {
    constructor(public rootLetters: RootLetters, public chartMode: ChartMode, public baseWord: string, public pastTenseRoot: string,
        public presentTenseRoot: string, public translation: string, public title: string, public typeLabel1: string,
        public typeLabel2: string, public typeLabel3: string) { }
}
