import { AbbreviatedConjugation } from './abbreviated-conjugation';
import { DetailedConjugation } from './detailed-conjugation';
import { RootLetters } from './root-letters';
import { NamedTemplate } from './named-template';

export class MorphologicalChart {
    private _abbreviatedConjugation!: AbbreviatedConjugation;
    private _detailedConjugation!: DetailedConjugation;
    private _rootLetters: RootLetters = new RootLetters();
    private _namedTemplate: NamedTemplate = NamedTemplate.FORM_I_CATEGORY_A_GROUP_A_TEMPLATE;
    private _id!: string;

    constructor(abbreviatedConjugation?: AbbreviatedConjugation, detailedConjugation?: DetailedConjugation) {
        if (abbreviatedConjugation) {
            this.abbreviatedConjugation = abbreviatedConjugation;
        }
        if (detailedConjugation) {
            this.detailedConjugation = detailedConjugation;
        }
    }

    public static of(src?: any) {
        const chart = new MorphologicalChart();
        if (src) {
            chart.abbreviatedConjugation = AbbreviatedConjugation.of(src.abbreviatedConjugation);
            chart.detailedConjugation = DetailedConjugation.of(src.detailedConjugation);
        }

        return chart;

    }

    get abbreviatedConjugation(): AbbreviatedConjugation {
        return this._abbreviatedConjugation;
    }

    set abbreviatedConjugation(value: AbbreviatedConjugation) {
        this._abbreviatedConjugation = value;
        if (this.abbreviatedConjugation) {
            this._rootLetters = this.abbreviatedConjugation.rootLetters;
            this._namedTemplate = this.abbreviatedConjugation.namedTemplate;
            this._id = this.abbreviatedConjugation.id;
        }
    }

    get detailedConjugation(): DetailedConjugation {
        return this._detailedConjugation;
    }

    set detailedConjugation(value: DetailedConjugation) {
        this._detailedConjugation = value;
    }

    get rootLetters(): RootLetters {
        return this._rootLetters;
    }

    get namedTemplate(): NamedTemplate {
        return this._namedTemplate;
    }

    get id(): string {
        return this._id;
    }

    equals(other: MorphologicalChart) {
        return this.abbreviatedConjugation && other && this.abbreviatedConjugation.equals(other.abbreviatedConjugation);
    }

    compareTo(other: MorphologicalChart): number {
        if (!this.abbreviatedConjugation && !other) {
            return 0;
        }
        return (other && this.abbreviatedConjugation.compareTo(other.abbreviatedConjugation)) || 1;
    }
}
