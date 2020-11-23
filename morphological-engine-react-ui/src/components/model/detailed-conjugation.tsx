import { IdGenerator } from '../../utils/id-generator';
import { RootLetters } from './root-letters';
import { NamedTemplate } from './named-template';
import { SarfTermType } from './sarf-term-type';

export class ConjugationTuple {
    static of(src?: any): ConjugationTuple | undefined {
        if (!src) {
            return undefined;
        }
        return new ConjugationTuple(src.singular, src.dual, src.plural);
    }

    constructor(public singular: string, public dual: string, public plural: string) { }

    public copy(): ConjugationTuple {
        return new ConjugationTuple(this.singular, this.dual, this.plural)
    }
}

export class ConjugationGroup {
    constructor(public termType: string) { }

    equals(other: ConjugationGroup): boolean {
        return other && this.termType === other.termType;
    }
}

export class VerbConjugationGroup extends ConjugationGroup {
    static of(src?: any): VerbConjugationGroup | undefined {
        if (!src) {
            return undefined;
        }
        return new VerbConjugationGroup(
            src.termType,
            ConjugationTuple.of(src.masculineSecondPerson)!,
            ConjugationTuple.of(src.feminineSecondPerson)!,
            ConjugationTuple.of(src.masculineThirdPerson),
            ConjugationTuple.of(src.feminineThirdPerson),
            ConjugationTuple.of(src.firstPerson)
        );
    }

    constructor(
        public termType: string,
        public masculineSecondPerson: ConjugationTuple,
        public feminineSecondPerson: ConjugationTuple,
        public masculineThirdPerson?: ConjugationTuple,
        public feminineThirdPerson?: ConjugationTuple,
        public firstPerson?: ConjugationTuple
    ) {
        super(termType);
    }

    public copy(): VerbConjugationGroup {
        return new VerbConjugationGroup(
            this.termType,
            this.masculineSecondPerson.copy(),
            this.feminineSecondPerson.copy(),
            this.masculineThirdPerson ? this.masculineThirdPerson.copy() : undefined,
            this.feminineThirdPerson ? this.feminineThirdPerson.copy() : undefined,
            this.firstPerson ? this.firstPerson.copy() : undefined
        )
    }

}

export class NounConjugationGroup extends ConjugationGroup {

    static of(src?: any): NounConjugationGroup | undefined {
        if (!src) {
            return undefined;
        }
        return new NounConjugationGroup(
            src.termType,
            ConjugationTuple.of(src.nominative)!,
            ConjugationTuple.of(src.accusative)!,
            ConjugationTuple.of(src.genitive)!
        );
    }

    constructor(
        public termType: string,
        public nominative: ConjugationTuple,
        public accusative: ConjugationTuple,
        public genitive: ConjugationTuple) {
        super(termType);
    }

    public copy(): NounConjugationGroup {
        return new NounConjugationGroup(
            this.termType,
            this.nominative.copy(),
            this.accusative.copy(),
            this.genitive.copy()
        )
    }
}

export class DetailedConjugation {
    static of(src?: any) {
        if (!src || !src.pastTense || !src.presentTense || !src.activeParticipleMasculine
            || !src.activeParticipleFeminine || !src.imperative || !src.forbidding) {
            throw new Error(`Invalid source: ${src}`);
        }
        return new DetailedConjugation(
            VerbConjugationGroup.of(src.pastTense)!,
            VerbConjugationGroup.of(src.presentTense)!,
            NounConjugationGroup.of(src.activeParticipleMasculine)!,
            NounConjugationGroup.of(src.activeParticipleFeminine)!,
            VerbConjugationGroup.of(src.imperative)!,
            VerbConjugationGroup.of(src.forbidding)!,
            VerbConjugationGroup.of(src.pastPassiveTense),
            VerbConjugationGroup.of(src.presentPassiveTense),
            NounConjugationGroup.of(src.passiveParticipleMasculine),
            NounConjugationGroup.of(src.passiveParticipleFeminine),
            [], [] // TODO:
        );
    }

    public id: string = IdGenerator.nextId();

    private _rootLetters: RootLetters = new RootLetters();
    private _namedTemplate: NamedTemplate = NamedTemplate.FORM_I_CATEGORY_A_GROUP_A_TEMPLATE;

    constructor(
        // active values
        public pastTense: VerbConjugationGroup,
        public presentTense: VerbConjugationGroup,
        public activeParticipleMasculine: NounConjugationGroup,
        public activeParticipleFeminine: NounConjugationGroup,

        // imperative and forbidden values
        public imperative: VerbConjugationGroup,
        public forbidding: VerbConjugationGroup,

        // passive values
        public pastPassiveTense?: VerbConjugationGroup,
        public presentPassiveTense?: VerbConjugationGroup,
        public passiveParticipleMasculine?: NounConjugationGroup,
        public passiveParticipleFeminine?: NounConjugationGroup,

        // verbal noun values
        public verbalNouns: NounConjugationGroup[] = [],
        // adverb values
        public adverbs: NounConjugationGroup[] = []) { }

    public copy(): DetailedConjugation {
        return new DetailedConjugation(
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
            this.verbalNouns ? this.verbalNouns.map((vn) => vn.copy()) : [],
            this.adverbs ? this.adverbs.map((ad) => ad.copy()) : []
        );
    }
    get rootLetters(): RootLetters {
        return this._rootLetters;
    }

    set rootLetters(value: RootLetters) {
        this._rootLetters = value;
        this.updateId();
    }

    get namedTemplate(): NamedTemplate {
        return this._namedTemplate;
    }

    set namedTemplate(value: NamedTemplate) {
        this._namedTemplate = value;
        this.updateId();
    }

    getConjugation(type: SarfTermType): NounConjugationGroup | VerbConjugationGroup | undefined {
        let result: NounConjugationGroup | VerbConjugationGroup | undefined = undefined;
        switch (type.name) {
            case SarfTermType.PAST_TENSE.name:
                result = this.pastTense;
                break;
            case SarfTermType.PRESENT_TENSE.name:
                result = this.presentTense;
                break;
            case SarfTermType.ACTIVE_PARTICIPLE_MASCULINE.name:
                result = this.activeParticipleMasculine;
                break;
            case SarfTermType.ACTIVE_PARTICIPLE_FEMININE.name:
                result = this.activeParticipleFeminine;
                break;
            case SarfTermType.PAST_PASSIVE_TENSE.name:
                result = this.pastPassiveTense;
                break;
            case SarfTermType.PRESENT_PASSIVE_TENSE.name:
                result = this.presentPassiveTense;
                break;
            case SarfTermType.PASSIVE_PARTICIPLE_MASCULINE.name:
                result = this.passiveParticipleMasculine;
                break;
            case SarfTermType.PASSIVE_PARTICIPLE_FEMININE.name:
                result = this.passiveParticipleFeminine;
                break;
            case SarfTermType.IMPERATIVE.name:
                result = this.imperative;
                break;
            case SarfTermType.FORBIDDING.name:
                result = this.forbidding;
                break;
            case SarfTermType.VERBAL_NOUN.name:
                let values = this.verbalNouns;
                if (values && values.length > 0) {
                    result = values[0];
                }
                break;
            case SarfTermType.NOUN_OF_PLACE_AND_TIME.name:
                values = this.adverbs;
                if (values && values.length > 0) {
                    result = values[0];
                }
                break;
        }
        return result;
    }

    setConjugation(type: SarfTermType, group: NounConjugationGroup | VerbConjugationGroup) {
        switch (type.name) {
            case SarfTermType.PAST_TENSE.name:
                this.pastTense = group as VerbConjugationGroup;
                break;
            case SarfTermType.PRESENT_TENSE.name:
                this.presentTense = group as VerbConjugationGroup;
                break;
            case SarfTermType.ACTIVE_PARTICIPLE_MASCULINE.name:
                this.activeParticipleMasculine = group as NounConjugationGroup;
                break;
            case SarfTermType.ACTIVE_PARTICIPLE_FEMININE.name:
                this.activeParticipleFeminine = group as NounConjugationGroup;
                break;
            case SarfTermType.PAST_PASSIVE_TENSE.name:
                this.pastPassiveTense = group as VerbConjugationGroup;
                break;
            case SarfTermType.PRESENT_PASSIVE_TENSE.name:
                this.presentPassiveTense = group as VerbConjugationGroup;
                break;
            case SarfTermType.PASSIVE_PARTICIPLE_MASCULINE.name:
                this.passiveParticipleMasculine = group as NounConjugationGroup;
                break;
            case SarfTermType.PASSIVE_PARTICIPLE_FEMININE.name:
                this.passiveParticipleFeminine = group as NounConjugationGroup;
                break;
            case SarfTermType.IMPERATIVE.name:
                this.imperative = group as VerbConjugationGroup;
                break;
            case SarfTermType.FORBIDDING.name:
                this.forbidding = group as VerbConjugationGroup;
                break;
            case SarfTermType.VERBAL_NOUN.name:
                this.updateValue(group as NounConjugationGroup, this.verbalNouns);
                break;
            case SarfTermType.NOUN_OF_PLACE_AND_TIME.name:
                this.updateValue(group as NounConjugationGroup, this.adverbs);
                break;
        }
    }

    equals(other: DetailedConjugation) {
        return other && this.id === other.id;
    }

    compareTo(other: DetailedConjugation): number {
        let result = this.namedTemplate.compareTo(other.namedTemplate);
        if (result === 0) {
            result = this.rootLetters.compareTo(other.rootLetters);
        }
        return result;
    }

    private updateId() {
        if (this.namedTemplate && this.rootLetters) {
            this.id = this.namedTemplate.name + '_' + this.rootLetters.name;
        } else {
            this.id = IdGenerator.nextId();
        }
    }

    private updateValue(value: NounConjugationGroup, values: NounConjugationGroup[]) {
        let index = -1;
        values.filter((v, i) => {
            if (v.equals(value)) {
                index = i;
                return true;
            } else {
                return false;
            }
        });
        if (index > -1) {
            values[index] = value;
        } else {
            values.push(value);
        }
    }
}
