import { AbbreviatedConjugation } from './abbreviated-conjugation';
import { DetailedConjugation } from './detailed-conjugation';
import { RootLetters } from './root-letters';
import { NamedTemplate } from './named-template';
import { ArabicLabel } from './arabic-label';
import { IdGenerator } from '../../utils/id-generator';

export class MorphologicalChartLabel extends ArabicLabel {

    constructor(public rootLetters: RootLetters, public namedTemplate: NamedTemplate, public id: string) {
        super(
            `${rootLetters.name}_${namedTemplate.name}`,
            `${rootLetters.label} - ${namedTemplate.label}`,
            `${rootLetters.name}_${namedTemplate.code}`
        );
    }

    public copy(): MorphologicalChartLabel {
        return new MorphologicalChartLabel(this.rootLetters.copy(), this.namedTemplate.copy(), this.id);
    }
}

export class MorphologicalChart {
    private _rootLetters: RootLetters = new RootLetters();
    private _namedTemplate: NamedTemplate = NamedTemplate.FORM_I_CATEGORY_A_GROUP_A_TEMPLATE;
    private _label: MorphologicalChartLabel;
    private _id!: string;

    constructor(
        public readonly abbreviatedConjugation: AbbreviatedConjugation,
        public readonly detailedConjugation: DetailedConjugation,
        id?: string
    ) {
        this._rootLetters = this.abbreviatedConjugation.rootLetters;
        this._namedTemplate = this.abbreviatedConjugation.namedTemplate;
        this._id = id ? id : IdGenerator.nextId();
        this._label = new MorphologicalChartLabel(this.rootLetters, this.namedTemplate, this.id);
    }

    public static of(src: any) {
        return new MorphologicalChart(
            AbbreviatedConjugation.of(src.abbreviatedConjugation),
            DetailedConjugation.of(src.detailedConjugation),
            src.id
        );
    }

    get rootLetters(): RootLetters {
        return this._rootLetters;
    }

    get namedTemplate(): NamedTemplate {
        return this._namedTemplate;
    }

    get label(): MorphologicalChartLabel {
        return this._label;
    }

    get id(): string {
        return this._id;
    }

    public copy(): MorphologicalChart {
        return new MorphologicalChart(
            this.abbreviatedConjugation.copy(),
            this.detailedConjugation.copy(),
            this.id
        );
    }

    equals(other: MorphologicalChart) {
        return this.id === other.id;
    }

    compareTo(other: MorphologicalChart): number {
        return this.id.localeCompare(other.id);
    }
}
