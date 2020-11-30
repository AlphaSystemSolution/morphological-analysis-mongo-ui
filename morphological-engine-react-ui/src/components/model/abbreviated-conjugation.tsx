import {v4 as uuid} from 'uuid';
import { ConjugationHeader } from './conjugation-header';
import { SarfTermType } from './sarf-term-type';
import { RootLetters } from './root-letters';
import { NamedTemplate } from './named-template';

export class ConjugationLabel {
    public static of(src?: any): ConjugationLabel | undefined {
        if (src) {
            return new ConjugationLabel(
                (src.type && SarfTermType.getByName(src.type)) || SarfTermType.PAST_TENSE,
                src.label,
                src.source
            );
        } else {
            return undefined;
        }
    }

    public static toArrayOfLabels(src?: any[]): ConjugationLabel[] {
        let result: ConjugationLabel[] = [];
        if (src) {
            result = src
                .filter((item: any) => item !== null || item !== undefined)
                .map((item: any) => ConjugationLabel.of(item)!);
        }
        return result;
    }

    constructor(
        public type: SarfTermType,
        public label: string,
        public code: string) { }

    public copy(): ConjugationLabel {
        return new ConjugationLabel(this.type.copy(), this.label, this.code);
    }

    public equals(other?: ConjugationLabel) {
        return other ? (this.type.equals(other.type) && this.label === other.label
            && this.code === other.code) : false;
    }
}

export class AbbreviatedConjugation {
    static of(src?: any) {
        if (!src) {
            throw new Error(`Invalid source: ${src}`);
        }
        const conjugationHeader = ConjugationHeader.of(src.conjugationHeader);
        return new AbbreviatedConjugation(
            src.id || uuid(),
            conjugationHeader,
            AbbreviatedConjugation.getRootLetters(conjugationHeader),
            AbbreviatedConjugation.getNamedTemplate(conjugationHeader),
            ConjugationLabel.of(src.pastTense)!,
            ConjugationLabel.of(src.presentTense)!,
            ConjugationLabel.of(src.activeParticipleMasculine)!,
            ConjugationLabel.of(src.activeParticipleFeminine)!,
            ConjugationLabel.of(src.imperative)!,
            ConjugationLabel.of(src.forbidding)!,
            ConjugationLabel.of(src.pastPassiveTense),
            ConjugationLabel.of(src.presentPassiveTense),
            ConjugationLabel.of(src.passiveParticipleMasculine),
            ConjugationLabel.of(src.passiveParticipleFeminine),
            ConjugationLabel.toArrayOfLabels(src.verbalNouns),
            ConjugationLabel.toArrayOfLabels(src.adverbs)
        );
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

    public copy(): AbbreviatedConjugation {
        return new AbbreviatedConjugation(
            this.id,
            this.conjugationHeader.copy(),
            this.rootLetters.copy(),
            this.namedTemplate.copy(),
            this.pastTense.copy(),
            this.presentTense.copy(),
            this.activeParticipleMasculine.copy(),
            this.activeParticipleFeminine.copy(),
            this.imperative.copy(),
            this.forbidding.copy(),
            this.pastPassiveTense ? this.pastPassiveTense.copy() : undefined,
            this.presentPassiveTense ? this.presentPassiveTense.copy() : undefined,
            this.passiveParticipleMasculine ? this.passiveParticipleMasculine.copy() : undefined,
            this.passiveParticipleFeminine ? this.passiveParticipleFeminine.copy() : undefined,
            this.verbalNouns.map((vn) => vn.copy()),
            this.adverbs.map((ad) => ad.copy())
        );
    }

    compareTo(other: AbbreviatedConjugation): number {
        let result = this.namedTemplate.compareTo(other.namedTemplate);
        if (result === 0) {
            result = this.rootLetters.compareTo(other.rootLetters);
        }
        return result;
    }
}
