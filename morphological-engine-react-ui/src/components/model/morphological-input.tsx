import { Document } from './document';
import { ConjugationConfiguration } from './models';
import { ArabicLetter } from './arabic-letter';
import { RootLetters } from './root-letters';
import { NamedTemplate } from './named-template';
import { VerbalNoun } from './verbal-noun';
import { NounOfPlaceAndTime } from './noun-of-place-and-time';
import { ConjugationData } from './conjugation-data';

export class MorphologicalInput extends Document {

    static copy(src: any, copyId: boolean): MorphologicalInput {
        const srcRootLetters = src.rootLetters || new RootLetters();
        const rootLetters: RootLetters = new RootLetters(srcRootLetters.firstRadical, srcRootLetters.secondRadical,
            srcRootLetters.thirdRadical, srcRootLetters.fourthRadical);
        const srcConjugationConfiguration = src.configuration || new ConjugationConfiguration();
        const conjugationConfiguration: ConjugationConfiguration = new ConjugationConfiguration(srcConjugationConfiguration.removePassiveLine,
            srcConjugationConfiguration.skipRuleProcessing);

        const result: MorphologicalInput = new MorphologicalInput(
            rootLetters,
            src.template || NamedTemplate.FORM_I_CATEGORY_A_GROUP_U_TEMPLATE,
            src.translation || "",
            conjugationConfiguration,
            src.verbalNouns || [],
            src.nounOfPlaceAndTimes || []
        );
        if (copyId) {
            result.id = src.id;
        }
        return result;
    }

    static createDefaultMorphologicalInput(): MorphologicalInput {
        return MorphologicalInput.copy({}, false);
    }

    static fromConjugationData(src: ConjugationData): MorphologicalInput {
        const result: MorphologicalInput = new MorphologicalInput();
        result.template = NamedTemplate.getByName(src.template);
        const rl = src.rootLetters;
        if (rl) {
            result.rootLetters = src.rootLetters.toRootLetters();
        }
        result.translation = src.translation;
        result.configuration = src.configuration;
        const _verbalNouns = src.verbalNouns;
        if (_verbalNouns) {
            const verbalNouns: VerbalNoun[] = [];
            _verbalNouns.forEach(vn => {
                const verbalNoun = VerbalNoun.getByName(vn);
                if (verbalNoun) {
                    verbalNouns.push(verbalNoun);
                }
            });
            result.verbalNouns = verbalNouns;
        }

        result.nounOfPlaceAndTimes = [];
        return result;
    }

    constructor(
        public rootLetters: RootLetters = new RootLetters(),
        public template: NamedTemplate = NamedTemplate.FORM_I_CATEGORY_A_GROUP_A_TEMPLATE,
        public translation: string = "",
        public configuration: ConjugationConfiguration = new ConjugationConfiguration(),
        public verbalNouns: VerbalNoun[] = [],
        public nounOfPlaceAndTimes: NounOfPlaceAndTime[] = []) {
        super();
    }

    get templateId(): string {
        return this.template.name + '_' + this.rootLetters.name;
    }

    get verbalNounsText(): string {
        let result = '';
        if (this.verbalNouns && this.verbalNouns.length > 0) {
            let verbalNoun = this.verbalNouns[0];
            result += verbalNoun.label;
            for (let i = 1; i < this.verbalNouns.length; i++) {
                verbalNoun = this.verbalNouns[i];
                result += ' و ' + verbalNoun.label;
            }
        }
        return result;
    }

    public compareTo(other: MorphologicalInput): number {
        let result = this.template.compareTo(other.template);
        if (result === 0) {
            result = this.rootLetters.compareTo(other.rootLetters);
        }
        return result;
    }
}
