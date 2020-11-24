import { Panel } from 'primereact/panel';
import * as React from 'react';
import { Utils } from '../../utils/utils';
import { DetailedConjugation, NounConjugationGroup } from '../model/detailed-conjugation';
import { ArabicConstants } from '../model/models';
import { NounConjugationGroupView } from './noun-conjugation-group-view';
import { VerbConjugationGroupView } from './verb-conjugation-group-view';

interface Props {
    conjugation: DetailedConjugation
}

interface State { }

export class DetailConjugationView extends React.Component<Props, State> {

    private renderArrayValues(groups: NounConjugationGroup[]) {
        if (groups.length <= 0) {
            return null;
        }
        const groupsChunked = Utils.chunkArray(groups.map((item) => item.copy()), 2);
        return groupsChunked.map((group, index) => {
            const group0 = group[0];
            const termType = group0.termType;
            if (group.length > 1) {
                return (
                    <React.Fragment>
                        <div className="p-col-12 p-md-6 p-lg-6" key={termType + "-0-" + index}>
                            <NounConjugationGroupView group={group0} />
                        </div>
                        <div className="p-col-12 p-md-6 p-lg-6" key={termType + "-1-" + index}>
                            <NounConjugationGroupView group={group[1]} />
                        </div>
                    </React.Fragment>
                );
            } else {
                return (
                    <React.Fragment>
                        <div className="p-col-12 p-md-6 p-lg-6" key={termType + "-0-" + index}>
                            <NounConjugationGroupView group={group0} />
                        </div>
                        <div className="p-col-12 p-md-6 p-lg-6" key={termType + "-1-" + index}>
                            <div>&nbsp;</div>
                        </div>
                    </React.Fragment>
                );
            }
        });
    }

    render() {
        const conjugation = this.props.conjugation;
        let passiveTenseLine = null;
        let passiveParticipleLine = null;
        if (conjugation.hasPassiveLine()) {
            passiveTenseLine = (
                <React.Fragment>
                    <div className="p-col-12 p-md-6 p-lg-6">
                        <VerbConjugationGroupView group={conjugation.pastPassiveTense!} />
                    </div>
                    <div className="p-col-12 p-md-6 p-lg-6">
                        <VerbConjugationGroupView group={conjugation.presentPassiveTense!} />
                    </div>
                </React.Fragment>
            );
            passiveParticipleLine = (
                <React.Fragment>
                    <div className="p-col-12 p-md-6 p-lg-6">
                        <NounConjugationGroupView group={conjugation.passiveParticipleMasculine!} />
                    </div>
                    <div className="p-col-12 p-md-6 p-lg-6">
                        <NounConjugationGroupView group={conjugation.passiveParticipleFeminine!} />
                    </div>
                </React.Fragment>
            );
        }

        return (
            <Panel header={ArabicConstants.DETAIL_CONJUGATION_LABEL.label} style={{ direction: 'rtl' }} toggleable>
                <div className="p-grid">
                    <div className="p-col-12 p-md-6 p-lg-6">
                        <VerbConjugationGroupView group={conjugation.pastTense} />
                    </div>
                    <div className="p-col-12 p-md-6 p-lg-6">
                        <VerbConjugationGroupView group={conjugation.presentTense} />
                    </div>
                    {this.renderArrayValues(conjugation.verbalNouns)}
                    <div className="p-col-12 p-md-6 p-lg-6">
                        <NounConjugationGroupView group={conjugation.activeParticipleMasculine} />
                    </div>
                    <div className="p-col-12 p-md-6 p-lg-6">
                        <NounConjugationGroupView group={conjugation.activeParticipleFeminine} />
                    </div>
                    {passiveTenseLine}
                    {passiveParticipleLine}
                    <div className="p-col-12 p-md-6 p-lg-6">
                        <VerbConjugationGroupView group={conjugation.imperative} />
                    </div>
                    <div className="p-col-12 p-md-6 p-lg-6">
                        <VerbConjugationGroupView group={conjugation.forbidding} />
                    </div>
                    {this.renderArrayValues(conjugation.adverbs)}
                </div>
            </Panel>
        );
    }
}