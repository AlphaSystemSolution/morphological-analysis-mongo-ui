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
        public empty?: boolean
    ) { }

    get label(): string {
        return this.firstRadical + this.secondRadical + this.thirdRadical;
    }

    public toRootLetters(): _RootLetters {
        return new _RootLetters(
            ArabicLetter.getByName(this.firstRadical),
            ArabicLetter.getByName(this.secondRadical),
            ArabicLetter.getByName(this.thirdRadical),
            this.fourthRadical ? ArabicLetter.getByName(this.firstRadical) : ArabicLetter.TATWEEL
        );
    }

    public static of(src?: any): RootLetters {
        if (!src) {
            throw new Error(`Unable to create "RootLetters" from: ${src}`);
        }
        return new RootLetters(
            src.id,
            src.name,
            src.firstRadical,
            src.secondRadical,
            src.thirdRadical,
            src.fourthRadical,
            src.displayName,
            src.empty
        );
    }
}

export class ChartMode {
    constructor(public template: string, public rootType: string, public verbType: string, public weakVerbType: string) { }

    public static of(src: any) {
        return new ChartMode(src.template, src.rootType, src.verbType, src.weakVerbType);
    }
}

export class ConjugationHeader {
    constructor(
        public rootLetters: RootLetters,
        public chartMode: ChartMode,
        public baseWord: string,
        public pastTenseRoot: string,
        public presentTenseRoot: string,
        public translation: string,
        public title: string,
        public typeLabel1: string,
        public typeLabel2: string,
        public typeLabel3: string
    ) { }

    public static of(src: any) {
        return new ConjugationHeader(
            RootLetters.of(src.rootLetters),
            ChartMode.of(src.chartMode),
            src.baseWord,
            src.pastTenseRoot,
            src.presentTenseRoot,
            src.translation,
            src.title,
            src.typeLabel1,
            src.typeLabel2,
            src.typeLabel3
        );
    }
}
