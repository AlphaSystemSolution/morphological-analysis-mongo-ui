import { ConjugationConfiguration, InputData } from './models';
import { Document } from './document';
import { RootLetters } from './conjugation-header';
import { NamedTemplate } from './named-template';
import { VerbalNoun } from './verbal-noun';

export class ConjugationData extends Document {

    constructor(
        public rootLetters: RootLetters,
        public template: string = NamedTemplate.FORM_I_CATEGORY_A_GROUP_U_TEMPLATE.name,
        public configuration: ConjugationConfiguration = new ConjugationConfiguration(),
        public translation: string = "",
        public verbalNouns: string[] = []) {
        super();
    }

    public toInputData(): InputData {
        return new InputData(
            this.rootLetters.toRootLetters(),
            NamedTemplate.getByName(this.template),
            this.translation,
            this.configuration.removePassiveLine,
            this.configuration.skipRuleProcessing,
            this.verbalNouns && this.verbalNouns.length > 0 ? this.verbalNouns.filter((name) => name != null).map((name) => VerbalNoun.getByName(name)) : []
        );
    }

    public static of(src?: any): ConjugationData {
        if (!src) {
            throw new Error(`Unable to create ConjugationData from: ${src}`);
        }
        return new ConjugationData(
            RootLetters.of(src.rootLetters),
            src.template,
            ConjugationConfiguration.of(src.configuration),
            src.translation,
            src.verbalNouns ? src.verbalNouns : []
        );
    }
}
