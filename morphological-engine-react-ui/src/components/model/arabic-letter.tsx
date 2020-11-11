import { ArabicLabel } from './models';

export class ArabicLetter extends ArabicLabel {
  static HAMZA = new ArabicLetter('HAMZA', 'ء', '\'');
  static ALIF = new ArabicLetter('ALIF', 'ا', 'A');
  static BA = new ArabicLetter('BA', 'ب', 'b');
  static TA = new ArabicLetter('TA', 'ت', 't');
  static THA = new ArabicLetter('THA', 'ث', 'v');
  static JEEM = new ArabicLetter('JEEM', 'ج', 'j');
  static HHA = new ArabicLetter('HHA', 'ح', 'H');
  static KHA = new ArabicLetter('KHA', 'خ', 'x');
  static DAL = new ArabicLetter('DAL', 'د', 'd');
  static THAL = new ArabicLetter('THAL', 'ذ', '*');
  static RA = new ArabicLetter('RA', 'ر', 'r');
  static ZAIN = new ArabicLetter('ZAIN', 'ز', 'z');
  static SEEN = new ArabicLetter('SEEN', 'س', 's');
  static SHEEN = new ArabicLetter('SHEEN', 'ش', '$');
  static SAD = new ArabicLetter('SAD', 'ص', 'S');
  static DDAD = new ArabicLetter('DDAD', 'ض', 'D');
  static TTA = new ArabicLetter('TTA', 'ط', 'T');
  static DTHA = new ArabicLetter('DTHA', 'ظ', 'Z');
  static AIN = new ArabicLetter('AIN', 'ع', 'E');
  static GHAIN = new ArabicLetter('GHAIN', 'غ', 'g');
  static FA = new ArabicLetter('FA', 'ف', 'f');
  static QAF = new ArabicLetter('QAF', 'ق', 'q');
  static KAF = new ArabicLetter('KAF', 'ك', 'k');
  static LAM = new ArabicLetter('LAM', 'ل', 'l');
  static MEEM = new ArabicLetter('MEEM', 'م', 'm');
  static NOON = new ArabicLetter('NOON', 'ن', 'n');
  static HA = new ArabicLetter('HA', 'ه', 'h');
  static WAW = new ArabicLetter('WAW', 'و', 'w');
  static YA = new ArabicLetter('YA', 'ي', 'y');
  static TATWEEL = new ArabicLetter('TATWEEL', 'ـ', '_');

  static arabicLetters: ArabicLetter[] = [
    ArabicLetter.HAMZA, // 0
    ArabicLetter.BA, // 1
    ArabicLetter.TA, // 2
    ArabicLetter.THA, // 3
    ArabicLetter.JEEM, // 4
    ArabicLetter.HHA, // 5
    ArabicLetter.KHA, // 6
    ArabicLetter.DAL, // 7
    ArabicLetter.THAL, // 8
    ArabicLetter.RA, // 9
    ArabicLetter.ZAIN, // 10
    ArabicLetter.SEEN, // 11
    ArabicLetter.SHEEN, // 12
    ArabicLetter.SAD, // 13
    ArabicLetter.DDAD, // 14
    ArabicLetter.TTA, // 15
    ArabicLetter.DTHA, // 16
    ArabicLetter.AIN, // 17
    ArabicLetter.GHAIN, // 18
    ArabicLetter.FA, // 19
    ArabicLetter.QAF, // 20
    ArabicLetter.KAF, // 21
    ArabicLetter.LAM, // 22
    ArabicLetter.MEEM, // 23
    ArabicLetter.NOON, // 24
    ArabicLetter.HA, // 25
    ArabicLetter.WAW, // 26
    ArabicLetter.YA, // 27
    ArabicLetter.TATWEEL, // 28
  ];

  static getByName(name: string): ArabicLetter {
    if (!name) {
      return ArabicLetter.TATWEEL;
    }
    const results = ArabicLetter.arabicLetters.filter(al => (al.name === name));
    return (results && results.length > 0) ? results[0] : ArabicLetter.TATWEEL;
  }

  constructor(public name: string, public label: string, public code: string) {
    super(name, label, code);
  }

  public compareTo(other: ArabicLabel): number {
    return this.label.localeCompare(other.label);
  }
}
