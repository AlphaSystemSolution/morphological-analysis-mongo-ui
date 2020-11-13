import { ArabicLabel } from './models';

export class NamedTemplate extends ArabicLabel {
  static FORM_I_CATEGORY_A_GROUP_U_TEMPLATE = new NamedTemplate('FORM_I_CATEGORY_A_GROUP_U_TEMPLATE', 'فَعَلَ يَفْعُلُ', 'Family I', 1, 1);
  static FORM_I_CATEGORY_A_GROUP_I_TEMPLATE = new NamedTemplate('FORM_I_CATEGORY_A_GROUP_I_TEMPLATE', 'فَعَلَ يَفْعِلُ', 'Family I', 1, 2);
  static FORM_I_CATEGORY_A_GROUP_A_TEMPLATE = new NamedTemplate('FORM_I_CATEGORY_A_GROUP_A_TEMPLATE', 'فَعَلَ يَفْعَلُ', 'Family I', 1, 3);
  static FORM_I_CATEGORY_U_TEMPLATE = new NamedTemplate('FORM_I_CATEGORY_U_TEMPLATE', 'فَعُلَ يَفْعُلُ', 'Family I', 1, 4);
  static FORM_I_CATEGORY_I_GROUP_A_TEMPLATE = new NamedTemplate('FORM_I_CATEGORY_I_GROUP_A_TEMPLATE', 'فَعِلَ يَفْعَلُ', 'Family I', 1, 5);
  static FORM_I_CATEGORY_I_GROUP_I_TEMPLATE = new NamedTemplate('FORM_I_CATEGORY_I_GROUP_I_TEMPLATE', 'فَعِلَ يَفْعِلُ', 'Family I', 1, 6);
  static FORM_II_TEMPLATE = new NamedTemplate('FORM_II_TEMPLATE', 'فَعَّلَ يُفَعِّلُ', 'Family II', 2, 2);
  static FORM_III_TEMPLATE = new NamedTemplate('FORM_III_TEMPLATE', 'فَاعَلَ يُفَاعِلُ', 'Family III', 2, 3);
  static FORM_IV_TEMPLATE = new NamedTemplate('FORM_IV_TEMPLATE', 'أَفْعَلَ يُفْعِلُ', 'Family IV', 2, 4);
  static FORM_V_TEMPLATE = new NamedTemplate('FORM_V_TEMPLATE', 'تَفَعَّلَ يَتَفَعَّلُ', 'Family V', 2, 5);
  static FORM_VI_TEMPLATE = new NamedTemplate('FORM_VI_TEMPLATE', 'تَفَاعَلَ يَتَفَاعَلُ', 'Family VI', 2, 6);
  static FORM_VII_TEMPLATE = new NamedTemplate('FORM_VII_TEMPLATE', 'إِنْفَعَلَ يَنْفَعِلُ', 'Family VII', 2, 7);
  static FORM_VIII_TEMPLATE = new NamedTemplate('FORM_VIII_TEMPLATE', 'إِفْتَعَلَ يَفْتَعِلُ', 'Family VIII', 2, 8);
  static FORM_IX_TEMPLATE = new NamedTemplate('FORM_IX_TEMPLATE', 'إِفْعَلَّ يَفْعَلَّ', 'Family IX', 2, 9);
  static FORM_X_TEMPLATE = new NamedTemplate('FORM_X_TEMPLATE', 'إِسْتَفْعَلَ يَسْتَفْعِلُ', 'Family X', 2, 10);

  static namedTemplates: NamedTemplate[] = [
    NamedTemplate.FORM_I_CATEGORY_A_GROUP_U_TEMPLATE,
    NamedTemplate.FORM_I_CATEGORY_A_GROUP_I_TEMPLATE,
    NamedTemplate.FORM_I_CATEGORY_A_GROUP_A_TEMPLATE,
    NamedTemplate.FORM_I_CATEGORY_U_TEMPLATE,
    NamedTemplate.FORM_I_CATEGORY_I_GROUP_A_TEMPLATE,
    NamedTemplate.FORM_I_CATEGORY_I_GROUP_I_TEMPLATE,
    NamedTemplate.FORM_II_TEMPLATE,
    NamedTemplate.FORM_III_TEMPLATE,
    NamedTemplate.FORM_IV_TEMPLATE,
    NamedTemplate.FORM_V_TEMPLATE,
    NamedTemplate.FORM_VI_TEMPLATE,
    NamedTemplate.FORM_VII_TEMPLATE,
    NamedTemplate.FORM_VIII_TEMPLATE,
    NamedTemplate.FORM_IX_TEMPLATE,
    NamedTemplate.FORM_X_TEMPLATE
  ];

  static getByName(name: string): NamedTemplate {
    if (!name) {
      return NamedTemplate.FORM_I_CATEGORY_A_GROUP_U_TEMPLATE;
    }
    const results = NamedTemplate.namedTemplates.filter(al => (al.name === name));
    return (results && results.length > 0) ? results[0] : NamedTemplate.FORM_I_CATEGORY_A_GROUP_U_TEMPLATE;
  }

  constructor(public name: string, public label: string, public code: string, private index: number, private subIndex: number) {
    super(name, label, code);
  }

  public compareTo(other: NamedTemplate): number {
    let result = this.index - other.index;
    if (result === 0) {
      result = this.subIndex - other.subIndex;
    }
    return result;
  }
}
