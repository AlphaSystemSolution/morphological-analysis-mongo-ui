import { AbbreviatedConjugation } from './abbreviated-conjugation';
import { DetailedConjugation } from './detailed-conjugation';
import { RootLetters } from './root-letters';
import { NamedTemplate } from './named-template';

export class MorphologicalChart {
    private _rootLetters: RootLetters = new RootLetters();
    private _namedTemplate: NamedTemplate = NamedTemplate.FORM_I_CATEGORY_A_GROUP_A_TEMPLATE;
    private _id!: string;

    constructor(public readonly abbreviatedConjugation: AbbreviatedConjugation, public readonly detailedConjugation: DetailedConjugation) {
        this._rootLetters = this.abbreviatedConjugation.rootLetters;
        this._namedTemplate = this.abbreviatedConjugation.namedTemplate;
        this._id = `${this.rootLetters.name}_${this.namedTemplate.name}`;
    }

    public static of(src: any) {
        return new MorphologicalChart(
            AbbreviatedConjugation.of(src.abbreviatedConjugation),
            DetailedConjugation.of(src.detailedConjugation)
        );
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
        return this.id === other.id;
    }

    compareTo(other: MorphologicalChart): number {
        return this.id.localeCompare(other.id);
    }
}
