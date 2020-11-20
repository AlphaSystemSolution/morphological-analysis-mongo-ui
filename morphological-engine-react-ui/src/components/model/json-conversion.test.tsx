import { assert } from 'console';
import { ConjugationLabel } from './abbreviated-conjugation';
import { ChartMode } from './conjugation-header';
import { ConjugationTuple, NounConjugationGroup } from './detailed-conjugation';
import { NamedTemplate } from './named-template';
import { SarfTermType } from './sarf-term-type';

const chartMode = {
    "template": "FORM_I_CATEGORY_A_GROUP_U_TEMPLATE",
    "rootType": "CONSONANT",
    "verbType": "CONSONANT",
    "weakVerbType": null
};

const conjugationLabel = {
    "id": "9E3D56E4",
    "type": "PAST_TENSE",
    "label": "فَعَلَ",
    "source": "FORM_I_PAST_TENSE_V1"
};

const conjugationLabels = [
    {
        "id": "5E5FDE8C",
        "type": "NOUN_OF_PLACE_AND_TIME",
        "label": "مَفْعَلٌ",
        "source": "NOUN_OF_PLACE_AND_TIME_V1"
    },
    {
        "id": "3E622A5A",
        "type": "NOUN_OF_PLACE_AND_TIME",
        "label": "مَفْعِلٌ",
        "source": "NOUN_OF_PLACE_AND_TIME_V2"
    },
    {
        "id": "9E59D6AC",
        "type": "NOUN_OF_PLACE_AND_TIME",
        "label": "مَفْعَلَةٌ",
        "source": "NOUN_OF_PLACE_AND_TIME_V3"
    }
];

const cojugationTuple = {
    "singular": "فَعَلَ",
    "dual": "فَعَلَا",
    "plural": "فَعَلُوْا"
};

const conjugationGroup = {
    "termType": "ACTIVE_PARTICIPLE_MASCULINE",
    "id": "9EDEB6F9",
    "nominative": {
        "singular": "فَاعِلٌ",
        "dual": "فَاعِلَانِ",
        "plural": "فَاعِلُوْنَ"
    },
    "accusative": {
        "singular": "فَاعِلًا",
        "dual": "فَاعِلَيْنِ",
        "plural": "فَاعِلِيْنَ"
    },
    "genitive": {
        "singular": "فَاعِلٍ",
        "dual": "فَاعِلَيْنِ",
        "plural": "فَاعِلِيْنَ"
    }
};

test('convert ChartMode', () => {
    const actual = ChartMode.of(chartMode);
    const expected = new ChartMode(
        NamedTemplate.FORM_I_CATEGORY_A_GROUP_U_TEMPLATE.name,
        'CONSONANT',
        'CONSONANT');
    console.log(`${JSON.stringify(expected)} === ${JSON.stringify(actual)}`);
    assert(actual.equals(expected));
});

test('convert ConjugationLabel', () => {
    const actual = ConjugationLabel.of(conjugationLabel)!;
    const expected = new ConjugationLabel(
        SarfTermType.PAST_TENSE,
        'فَعَلَ',
        'FORM_I_PAST_TENSE_V1'
    );
    console.log(`${JSON.stringify(expected)} === ${JSON.stringify(actual)}`);
    assert(actual.equals(expected));
});

test('convert multiple ConjugationLabel', () => {
    const actual = ConjugationLabel.toArrayOfLabels(conjugationLabels)!;
    console.log(`${JSON.stringify(actual)}`);
});

test('convert ConjugationTuple', () => {
    const actual = ConjugationTuple.of(cojugationTuple);
    console.log(JSON.stringify(actual));
});

test('convert ConjugationGroup', () => {
    const actual = NounConjugationGroup.of(conjugationGroup);
    console.log(JSON.stringify(actual));
});
