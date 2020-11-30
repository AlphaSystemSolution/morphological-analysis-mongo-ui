import { IdGenerator } from '../../utils/id-generator';
import { Utils } from '../../utils/utils';
import { ArabicLabel } from './arabic-label';
import { ChartConfiguration } from './chart-configuration';
import { ConjugationData } from './conjugation-data';
import { NamedTemplate } from './named-template';
import { RootLetters } from './root-letters';
import { VerbalNoun } from './verbal-noun';

export class ConjugationConfiguration {
  constructor(public removePassiveLine: boolean = false, public skipRuleProcessing: boolean = false) { }

  public static of(src?: any): ConjugationConfiguration {
    return src ? new ConjugationConfiguration(src.removePassiveLine, src.skipRuleProcessing) : new ConjugationConfiguration();
  }
}

export class NounStatus extends ArabicLabel {

  static NOMINATIVE: NounStatus = new NounStatus('NOMINATIVE', 'مرفوع', 'Nominative');
  static ACCUSATIVE: NounStatus = new NounStatus('ACCUSATIVE', 'منصوب', 'Accusative');
  static GENITIVE: NounStatus = new NounStatus('GENITIVE', 'مجرور', 'Genitive');

  static VALUES: NumberType[] = [NounStatus.NOMINATIVE, NounStatus.ACCUSATIVE, NounStatus.GENITIVE];

  constructor(public name: string, public label: string, public code: string) {
    super(name, label, code);
  }
}

export class NumberType extends ArabicLabel {

  static SINGULAR: NumberType = new NumberType('SINGULAR', 'مفرد', 'Singular');
  static DUAL: NumberType = new NumberType('PAIR', 'مثنّى', 'Pair');
  static PLURAL: NumberType = new NumberType('PLURAL', 'جمع', 'Plural');

  static VALUES: NumberType[] = [NumberType.SINGULAR, NumberType.DUAL, NumberType.PLURAL];

  constructor(public name: string, public label: string, public code: string) {
    super(name, label, code);
  }
}

export class ConversationType extends ArabicLabel {

  static THIRD_PERSON_MSCULINE: ConversationType = new ConversationType('THIRD_PERSON_MSCULINE', 'مذكر غائب', 'Third Person Masculine');
  static THIRD_PERSON_FEMININE: ConversationType = new ConversationType('THIRD_PERSON_FEMININE', 'مؤنّث غائب', 'Third Person Feminine');
  static SECOND_PERSON_MSCULINE: ConversationType = new ArabicLabel('SECOND_PERSON_MSCULINE', 'مذكر مخاطب', 'Second Person Masculine');
  static SECOND_PERSON_FEMININE: ConversationType = new ArabicLabel('SECOND_PERSON_FEMININE', 'مؤنّث مخاطب', 'Second Person Feminine');
  static FIRST_PERSON: ConversationType = new ArabicLabel('FIRST_PERSON', 'متكلّم', 'First Person');

  constructor(public name: string, public label: string, public code: string) {
    super(name, label, code);
  }
}

export enum DisplayType {
  LABEL_ONLY, CODE_ONLY, LABEL_AND_CODE
}

export enum OutputFormat {
  UNICODE, HTML, BUCK_WALTER, STREAM
}

export class ArabicConstants {
  static PARTICIPLE_PREFIX = new ArabicLabel('PARTICIPLE_PREFIX', 'فهو', 'Participle prefix');
  static IMPERATIVE_PREFIX = new ArabicLabel('IMPERATIVE_PREFIX', 'الأمر منه', 'Imperative prefix');
  static FORBIDDING_PREFIX = new ArabicLabel('FORBIDDING_PREFIX', 'ونهي عنه', 'Forbidding prefix');
  static ADVERBS_PREFIX = new ArabicLabel('ADVERBS_PREFIX', 'والظرف منه', 'Adverbs prefix');
  static ABBREVIATED_CONJUGATION_LABEL = new ArabicLabel('ABBREVIATED_CONJUGATION', 'صرف صغير', 'Abbreviated Conjugation');
  static DETAIL_CONJUGATION_LABEL = new ArabicLabel('DETAIL_CONJUGATION', 'صرف كبير', 'Detail Conjugation');
  static AND = new ArabicLabel('AND', 'و', 'AND');
  static AND_SPACE = new ArabicLabel('AND_SPACE', ' و ', 'AND_SPACE');
}

export class Project {
  constructor(
    public projectName: string,
    public fileName: string,
    public data: InputData[] = [],
    public chartConfiguration: ChartConfiguration = new ChartConfiguration()) { }
}

export class InputData {
  constructor(
    public rootLetters: RootLetters = new RootLetters(),
    public family: NamedTemplate = NamedTemplate.FORM_I_CATEGORY_A_GROUP_U_TEMPLATE,
    public translation: string = "",
    public removePassiveLine: boolean = false,
    public skipRuleProcessing: boolean = false,
    public verbalNouns: VerbalNoun[] = [],
    public id: string = IdGenerator.nextId()
  ) { }

  public toConjugationData() {
    return new ConjugationData(
      this.rootLetters.toRootLetters(),
      this.family.name,
      new ConjugationConfiguration(this.removePassiveLine, this.skipRuleProcessing),
      this.translation,
      this.verbalNouns.map((vn) => vn.name),
      this.id
    );
  }

  public copy(): InputData {
    return new InputData(
      this.rootLetters.copy(),
      this.family.copy(),
      this.translation,
      this.removePassiveLine,
      this.skipRuleProcessing,
      Utils.copyArray(this.verbalNouns),
      this.id
    );
  }
}

export enum ExportType {
  BOTH, ABBREVIATED_CONJUGATION, DETAILED_CONJUGATION
}
