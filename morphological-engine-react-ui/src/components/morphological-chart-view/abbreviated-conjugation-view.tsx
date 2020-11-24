import * as React from 'react';
import { AbbreviatedConjugation, ConjugationLabel } from '../model/abbreviated-conjugation';
import { ArabicConstants } from '../model/models';

interface Props {
    conjugation?: AbbreviatedConjugation
}

interface State { }

export class AbbreviatedConjugationView extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);

        this.state = {}
    }

    private renderActiveLine(abbreviatedConjugation: AbbreviatedConjugation, verbalNouns: any) {
        return (
            <React.Fragment>
                <div className="p-col-3 p-md-12 p-lg-3">
                    <div className="box">
                        {abbreviatedConjugation.pastTense.label}
                    </div>
                </div>
                <div className="p-col-3 p-md-12 p-lg-3">
                    <div className="box">
                        {abbreviatedConjugation.presentTense.label}
                    </div>
                </div>
                <div className="p-col-3 p-md-12 p-lg-3">
                    <div className="box">
                        {verbalNouns}
                    </div>
                </div>
                <div className="p-col-3 p-md-12 p-lg-3">
                    <div className="box">
                        <span className="arabicDisabled">{ArabicConstants.PARTICIPLE_PREFIX.label}</span>&nbsp;
                                    <span>{abbreviatedConjugation.activeParticipleMasculine.label}</span>
                    </div>
                </div>
            </React.Fragment>
        );
    }

    private renderMultiValues(labels: ConjugationLabel[]) {
        if (labels.length <= 0) {
            return <div className="box">&nbsp;</div>
        } else {
            const values = [];
            values.push(<span key="item-0">{labels[0].label}</span>);
            labels.shift();
            labels.forEach((item, index) => {
                values.push(<span key={"item-separator-" + (index + 1)} className="arabicDisabled">{ArabicConstants.AND_SPACE.label}</span>);
                values.push(<span key={"item-" + (index + 2)}>{item.label}</span>);
            });
            return (
                <React.Fragment>
                    {values}
                </React.Fragment>
            );
        }
    }

    private renderPassiveLine(abbreviatedConjugation: AbbreviatedConjugation, verbalNouns: any) {
        return (
            <React.Fragment>
                <div className="p-col-3 p-md-12 p-lg-3">
                    <div className="box">
                        {abbreviatedConjugation.pastPassiveTense!.label}
                    </div>
                </div>
                <div className="p-col-3 p-md-12 p-lg-3">
                    <div className="box">
                        {abbreviatedConjugation.presentPassiveTense!.label}
                    </div>
                </div>
                <div className="p-col-3 p-md-12 p-lg-3">
                    <div className="box">
                        {verbalNouns}
                    </div>
                </div>
                <div className="p-col-3 p-md-12 p-lg-3">
                    <div className="box">
                        <span className="arabicDisabled">{ArabicConstants.PARTICIPLE_PREFIX.label}</span>&nbsp;
                                    <span>{abbreviatedConjugation.passiveParticipleMasculine!.label}</span>
                    </div>
                </div>
            </React.Fragment>
        );
    }

    private renderImperativeAndForbiddenLine(abbreviatedConjugation: AbbreviatedConjugation) {
        return (
            <React.Fragment>
                <div className="p-col-6 p-md-12 p-lg-6">
                    <div className="box">
                        <span className="arabicDisabled">
                            {ArabicConstants.IMPERATIVE_PREFIX.label}
                        </span>&nbsp;
                        {abbreviatedConjugation.imperative.label}
                    </div>
                </div>
                <div className="p-col-6 p-md-12 p-lg-6">
                    <div className="box">
                        <span className="arabicDisabled">
                            {ArabicConstants.FORBIDDING_PREFIX.label}
                        </span>&nbsp;
                        {abbreviatedConjugation.forbidding.label}
                    </div>
                </div>
            </React.Fragment>
        );
    }

    private renderAdverbs(labels: ConjugationLabel[]) {
        if (labels.length <= 0) {
            return null;
        } else {
            return (
                <React.Fragment>
                    <div className="p-col-12 p-md-12 p-lg-12">
                        <div className="box">
                            <span className="arabicDisabled">
                                {ArabicConstants.ADVERBS_PREFIX.label}
                            </span>&nbsp;
                            {this.renderMultiValues(labels)}
                        </div>
                    </div>
                </React.Fragment>
            );
        }

    }

    render() {
        const abbreviatedConjugation = this.props.conjugation;
        if (abbreviatedConjugation) {
            const verbalNouns: any = this.renderMultiValues(abbreviatedConjugation.verbalNouns.map((item) => item.copy()));
            let passiveLine = null;
            if (abbreviatedConjugation.hasPassiveLine()) {
                passiveLine = this.renderPassiveLine(abbreviatedConjugation, verbalNouns);
            }
            return (
                <React.Fragment>
                    <div className="p-grid arabicNormal">
                        {this.renderActiveLine(abbreviatedConjugation, verbalNouns)}
                        {passiveLine}
                        {this.renderImperativeAndForbiddenLine(abbreviatedConjugation)}
                        {this.renderAdverbs(abbreviatedConjugation.adverbs.map((item) => item.copy()))}
                    </div>
                    <div>&nbsp;</div>
                </React.Fragment>
            );
        } else {
            return <div>&nbsp;</div>
        }

    }
}