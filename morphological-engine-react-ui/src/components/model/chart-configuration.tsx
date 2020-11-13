import { Document } from './models';

export enum PageOrientation {
    PORTRAIT, LANDSCAPE
}

export class PageOption extends Document {

    constructor(public orientation: string) {
        super();
    }
}

export class ChartConfiguration extends Document {

    omitToc = false;
    omitAbbreviatedConjugation = false;
    omitDetailedConjugation = false;
    omitTitle = false;
    omitHeader = false;
    omitSarfTermCaption = false;
    sortDirective = 'NONE';
    sortDirection = 'ASCENDING';
    arabicFontFamily = 'KFGQPC Uthman Taha Naskh';
    translationFontFamily = 'Candara';
    arabicFontSize = 14;
    translationFontSize = 12;
    headingFontSize = 30;
    pageOption: PageOption;

    constructor() {
        super();
        this.pageOption = new PageOption(PageOrientation[PageOrientation.PORTRAIT]);
    }
}
