import { ConjugationHeader } from './conjugation-header';
import { ArabicLabel } from './arabic-label';
import { SarfTermType } from './sarf-term-type';
import { RootLetters } from './root-letters';
import { NamedTemplate } from './named-template';
import { IdGenerator } from '../../utils/id-generator';

export class ConjugationLabel extends ArabicLabel {
    static of(src?: any) {
        if(src) {
            return new ConjugationLabel(
                src.id || IdGenerator.nextId(),
                (src.type && SarfTermType.getByName(src.type)) || SarfTermType.PAST_TENSE,
                src.source,
                src.label,
                src.source
            );
        } else {
            return undefined;
        }
    }

    constructor(
        public id: string,
        public type: SarfTermType,
        public name: string,
        public label: string,
        public code: string) {
        super(name, label, code)
    }
}

export class AbbreviatedConjugation {
    static of(src?: any) {
        if (!src) {
            throw new Error(`Invalid source: ${src}`);
        }
        const conjugationHeader = ConjugationHeader.of(src.conjugationHeader);
        return new AbbreviatedConjugation(
            src.id || IdGenerator.nextId(),
            conjugationHeader,
            AbbreviatedConjugation.getRootLetters(conjugationHeader),
            AbbreviatedConjugation.getNamedTemplate(conjugationHeader),
            ConjugationLabel.of(src.pastTense)!,
            ConjugationLabel.of(src.presentTense)!,
            ConjugationLabel.of(src.activeParticipleMasculine)!,
            ConjugationLabel.of(src.activeParticipleFeminine)!,
            ConjugationLabel.of(src.imperative)!,
            ConjugationLabel.of(src.imperative)!,
            ConjugationLabel.of(src.pastPassiveTense),
            ConjugationLabel.of(src.presentPassiveTense),
            ConjugationLabel.of(src.passiveParticipleMasculine),
            ConjugationLabel.of(src.passiveParticipleFeminine),
            AbbreviatedConjugation.getLabels(src.verbalNouns),
            AbbreviatedConjugation.getLabels(src.advebs)
        );
    }

    private static getLabels(values: any[]): ConjugationLabel[] {
        if (!values) {
            return [];
        }
        const labels: ConjugationLabel[] = [];
        values.forEach(value => {
            const label = ConjugationLabel.of(value);
            if (label != null) {
                labels.push(label);
            }
        });
        return labels;
    }

    private static getRootLetters(conjugationHeader: ConjugationHeader): RootLetters {
        return conjugationHeader.rootLetters.toRootLetters();
    }

    private static getNamedTemplate(conjugationHeader: ConjugationHeader): NamedTemplate {
        return NamedTemplate.getByName(conjugationHeader.chartMode.template);
    }

    constructor(
        public id: string,
        public conjugationHeader: ConjugationHeader,
        public rootLetters: RootLetters,
        public namedTemplate: NamedTemplate,
        public pastTense: ConjugationLabel,
        public presentTense: ConjugationLabel,
        public activeParticipleMasculine: ConjugationLabel,
        public activeParticipleFeminine: ConjugationLabel,
        public imperative: ConjugationLabel,
        public forbidding: ConjugationLabel,
        public pastPassiveTense?: ConjugationLabel,
        public presentPassiveTense?: ConjugationLabel,
        public passiveParticipleMasculine?: ConjugationLabel,
        public passiveParticipleFeminine?: ConjugationLabel,
        public verbalNouns: ConjugationLabel[] = [],
        public adverbs: ConjugationLabel[] = []) { }

    public hasPassiveLine() {
        return this.pastPassiveTense && this.presentPassiveTense && this.passiveParticipleMasculine; 
    }

    equals(other: AbbreviatedConjugation) {
        return other && this.id === other.id;
    }

    compareTo(other: AbbreviatedConjugation): number {
        let result = this.namedTemplate.compareTo(other.namedTemplate);
        if (result === 0) {
            result = this.rootLetters.compareTo(other.rootLetters);
        }
        return result;
    }
}
