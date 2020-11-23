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
            <tr>
                <td className="morphological-chart">
                    <span>{abbreviatedConjugation.pastTense.label}</span>
                </td>
                <td className="morphological-chart">
                    <span>{abbreviatedConjugation.presentTense.label}</span>
                </td>
                <td className="morphological-chart">
                    {verbalNouns}
                </td>
                <td className="morphological-chart">
                    <span>
                        <span className="arabicDisabled">{ArabicConstants.PARTICIPLE_PREFIX.label}</span>&nbsp;
                                    <span>{abbreviatedConjugation.activeParticipleMasculine.label}</span>
                    </span>
                </td>
            </tr>
        );
    }

    private renderMultiValues(labels: ConjugationLabel[]) {
        if (labels.length <= 0) {
            return <span>&nbsp;</span>
        } else {
            const values = [];
            values.push(<span key="item-0">{labels[0].label}</span>);
            labels.shift();
            labels.forEach((item, index) => {
                values.push(<span key={"item-separator-" + (index + 1)} className="arabicDisabled">{ArabicConstants.AND_SPACE.label}</span>);
                values.push(<span key={"item-" + (index + 2)}>{item.label}</span>);
            });
            return (
                <span>
                    {values}
                </span>
            );
        }
    }

    private renderPassiveLine(abbreviatedConjugation: AbbreviatedConjugation, verbalNouns: any) {
        return (
            <tr>
                <td className="morphological-chart">
                    <span>{abbreviatedConjugation.pastPassiveTense!.label}</span>
                </td>
                <td className="morphological-chart">
                    <span>{abbreviatedConjugation.presentPassiveTense!.label}</span>
                </td>
                <td className="morphological-chart">
                    {verbalNouns}
                </td>
                <td className="morphological-chart">
                    <span>
                        <span className="arabicDisabled">{ArabicConstants.PARTICIPLE_PREFIX.label}</span>&nbsp;
                        <span>{abbreviatedConjugation.passiveParticipleMasculine!.label}</span>
                    </span>
                </td>
            </tr>
        );
    }

    private renderImperativeAndForbiddenLine(abbreviatedConjugation: AbbreviatedConjugation) {
        return (
            <tr>
                <td className="morphological-chart" colSpan={2}>
                    <span>
                        <span className="arabicDisabled">{ArabicConstants.IMPERATIVE_PREFIX.label}</span>&nbsp;
                                    <span>{abbreviatedConjugation.imperative.label}</span>
                    </span>
                </td>
                <td className="morphological-chart" colSpan={2}>
                    <span>
                        <span className="arabicDisabled">{ArabicConstants.FORBIDDING_PREFIX.label}</span>&nbsp;
                                    <span>{abbreviatedConjugation.forbidding.label}</span>
                    </span>
                </td>
            </tr>
        );
    }

    private renderAdverbs(labels: ConjugationLabel[]) {
        return (
            <span>
                <span className="arabicDisabled">{ArabicConstants.ADVERBS_PREFIX.label}</span>
                <span className="arabicDisabled">{ArabicConstants.AND_SPACE.label}</span>
                {this.renderMultiValues(labels)}
            </span>
        );
    }

    render() {
        const abbreviatedConjugation = this.props.conjugation;
        if (abbreviatedConjugation) {
            const verbalNouns: any = this.renderMultiValues(abbreviatedConjugation.verbalNouns);
            let secondAndThirdRows = undefined;
            if (abbreviatedConjugation.hasPassiveLine()) {
                secondAndThirdRows = (
                    <React.Fragment>
                        {this.renderPassiveLine(abbreviatedConjugation, verbalNouns)}
                        {this.renderImperativeAndForbiddenLine(abbreviatedConjugation)}
                    </React.Fragment>
                );
            } else {
                secondAndThirdRows = (
                    <React.Fragment>
                        {this.renderImperativeAndForbiddenLine(abbreviatedConjugation)}
                    </React.Fragment>
                );
            }
            return (
                <table className="morphological-chart arabicNormal">
                    <tbody>
                        <tr>
                            <td colSpan={4} className="morphological-chart-no-border arabicTitle">
                                {abbreviatedConjugation.conjugationHeader.title}
                            </td>
                        </tr>
                        {this.renderActiveLine(abbreviatedConjugation, verbalNouns)}
                        {secondAndThirdRows}
                        <tr>
                            <td className="morphological-chart" colSpan={4}>
                                {this.renderAdverbs(abbreviatedConjugation.adverbs)}
                            </td>
                        </tr>
                        <tr>
                            <td className="morphological-chart-no-border" colSpan={4}>
                                <span>&nbsp;</span>
                            </td>
                        </tr>
                    </tbody>
                </table>
            );
        } else {
            return <div>&nbsp;</div>
        }

    }
}