import { ArabicLabel } from './arabic-label';

export class SarfTermType extends ArabicLabel {

  static PAST_TENSE = new SarfTermType('PAST_TENSE', 'فعل ماضي');
  static PRESENT_TENSE = new SarfTermType('PRESENT_TENSE', 'فعل مضارع');
  static VERBAL_NOUN = new SarfTermType('VERBAL_NOUN', 'مصدر');
  static ACTIVE_PARTICIPLE_MASCULINE = new SarfTermType('ACTIVE_PARTICIPLE_MASCULINE', 'إسم فاعل مذكر');
  static ACTIVE_PARTICIPLE_FEMININE = new SarfTermType('ACTIVE_PARTICIPLE_FEMININE', 'إسم فاعل مؤنث');
  static PAST_PASSIVE_TENSE = new SarfTermType('PAST_PASSIVE_TENSE', 'فعل ماضي مبني على المجهول');
  static PRESENT_PASSIVE_TENSE = new SarfTermType('PRESENT_PASSIVE_TENSE', 'فعل مضارع مبني على المجهول');
  static PASSIVE_PARTICIPLE_MASCULINE = new SarfTermType('PASSIVE_PARTICIPLE_MASCULINE', 'إسم مفعول مذكر');
  static PASSIVE_PARTICIPLE_FEMININE = new SarfTermType('PASSIVE_PARTICIPLE_FEMININE', 'إسم مفعول مؤنث');
  static IMPERATIVE = new SarfTermType('IMPERATIVE', 'أمر');
  static FORBIDDING = new SarfTermType('FORBIDDING', 'نهي');
  static NOUN_OF_PLACE_AND_TIME = new SarfTermType('NOUN_OF_PLACE_AND_TIME', 'ظرف');

  static getByName(name: string): SarfTermType {
    let result: SarfTermType = SarfTermType.PAST_TENSE;
    switch (name) {
      case 'PAST_TENSE':
        result = SarfTermType.PAST_TENSE;
        break;
      case 'PRESENT_TENSE':
        result = SarfTermType.PRESENT_TENSE;
        break;
      case 'VERBAL_NOUN':
        result = SarfTermType.VERBAL_NOUN;
        break;
      case 'ACTIVE_PARTICIPLE_MASCULINE':
        result = SarfTermType.ACTIVE_PARTICIPLE_MASCULINE;
        break;
      case 'ACTIVE_PARTICIPLE_FEMININE':
        result = SarfTermType.ACTIVE_PARTICIPLE_FEMININE;
        break;
      case 'PAST_PASSIVE_TENSE':
        result = SarfTermType.PAST_PASSIVE_TENSE;
        break;
      case 'PRESENT_PASSIVE_TENSE':
        result = SarfTermType.PRESENT_PASSIVE_TENSE;
        break;
      case 'PASSIVE_PARTICIPLE_MASCULINE':
        result = SarfTermType.PASSIVE_PARTICIPLE_MASCULINE;
        break;
      case 'PASSIVE_PARTICIPLE_FEMININE':
        result = SarfTermType.PASSIVE_PARTICIPLE_FEMININE;
        break;
      case 'IMPERATIVE':
        result = SarfTermType.IMPERATIVE;
        break;
      case 'FORBIDDING':
        result = SarfTermType.FORBIDDING;
        break;
      case 'NOUN_OF_PLACE_AND_TIME':
        result = SarfTermType.NOUN_OF_PLACE_AND_TIME;
        break;
    }
    return result;
  }

  constructor(public name: string, public label: string) {
    super(name, label, name);
  }

  get isVerbType(): boolean {
    return this.name === SarfTermType.PAST_TENSE.name || this.name === SarfTermType.PRESENT_TENSE.name
      || this.name === SarfTermType.PAST_PASSIVE_TENSE.name || this.name === SarfTermType.PRESENT_PASSIVE_TENSE.name
      || this.name === SarfTermType.IMPERATIVE.name || this.name === SarfTermType.FORBIDDING.name;
  }
}
