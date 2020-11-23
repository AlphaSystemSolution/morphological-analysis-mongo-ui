import { Document } from './document';

export enum PageOrientation {
    PORTRAIT, LANDSCAPE
}

export class PageOption extends Document {

    constructor(public orientation: string = PageOrientation[PageOrientation.PORTRAIT]) {
        super();
    }

    public static of(src?: any): PageOption {
        return src ? new PageOption(PageOrientation[src.orientation]) : new PageOption()
    }
}

export class ChartConfiguration extends Document {

    constructor(
        public omitToc = false,
        public omitAbbreviatedConjugation = false,
        public omitDetailedConjugation = false,
        public omitTitle = false,
        public omitHeader = false,
        public omitSarfTermCaption = false,
        public sortDirective = 'NONE',
        public sortDirection = 'ASCENDING',
        public arabicFontFamily = 'KFGQPC Uthman Taha Naskh',
        public translationFontFamily = 'Candara',
        public arabicFontSize = 14,
        public translationFontSize = 12,
        public headingFontSize = 30,
        public pageOption: PageOption = new PageOption()) {
        super();
    }

    public static of(src?: any): ChartConfiguration {
        if (src) {
            return new ChartConfiguration(
                src.omitToc,
                src.omitAbbreviatedConjugation,
                src.omitDetailedConjugation,
                src.omitTitle,
                src.omitHeader,
                src.omitSarfTermCaption,
                src.sortDirective,
                src.sortDirection,
                src.arabicFontFamily,
                src.translationFontFamily,
                src.arabicFontSize,
                src.translationFontSize,
                src.headingFontSize,
                PageOption.of(src.pageOption)
            );
        }
        return new ChartConfiguration();
    }
}
