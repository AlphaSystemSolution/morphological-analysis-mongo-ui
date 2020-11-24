import * as React from 'react';
import { Utils } from '../../utils/utils';
import { DetailedConjugation, NounConjugationGroup } from '../model/detailed-conjugation';
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
        return groupsChunked.map((group) => {
            if (group.length > 1) {
                return (
                    <React.Fragment>
                        <div className="p-col-12 p-md-6 p-lg-6">
                            <NounConjugationGroupView group={group[1]} />
                        </div>
                        <div className="p-col-12 p-md-6 p-lg-6">
                            <NounConjugationGroupView group={group[0]} />
                        </div>
                    </React.Fragment>
                );
            } else {
                return (
                    <React.Fragment>
                        <div className="p-col-12 p-md-6 p-lg-6">
                            <div>&nbsp;</div>
                        </div>
                        <div className="p-col-12 p-md-6 p-lg-6">
                            <NounConjugationGroupView group={group[0]} />
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
                        <VerbConjugationGroupView group={conjugation.presentPassiveTense!} />
                    </div>
                    <div className="p-col-12 p-md-6 p-lg-6">
                        <VerbConjugationGroupView group={conjugation.pastPassiveTense!} />
                    </div>
                </React.Fragment>
            );
            passiveParticipleLine = (
                <React.Fragment>
                    <div className="p-col-12 p-md-6 p-lg-6">
                        <NounConjugationGroupView group={conjugation.passiveParticipleFeminine!} />
                    </div>
                    <div className="p-col-12 p-md-6 p-lg-6">
                        <NounConjugationGroupView group={conjugation.passiveParticipleMasculine!} />
                    </div>
                </React.Fragment>
            );
        }

        return (
            <div className="p-grid">
                <div className="p-col-12 p-md-6 p-lg-6">
                    <VerbConjugationGroupView group={conjugation.presentTense} />
                </div>
                <div className="p-col-12 p-md-6 p-lg-6">
                    <VerbConjugationGroupView group={conjugation.pastTense} />
                </div>
                <div className="p-col-12 p-md-6 p-lg-6">
                    <NounConjugationGroupView group={conjugation.activeParticipleFeminine} />
                </div>
                <div className="p-col-12 p-md-6 p-lg-6">
                    <NounConjugationGroupView group={conjugation.activeParticipleMasculine} />
                </div>
                {this.renderArrayValues(conjugation.verbalNouns)}
                {passiveTenseLine}
                {passiveParticipleLine}
                <div className="p-col-12 p-md-6 p-lg-6">
                    <VerbConjugationGroupView group={conjugation.forbidding} />
                </div>
                <div className="p-col-12 p-md-6 p-lg-6">
                    <VerbConjugationGroupView group={conjugation.imperative} />
                </div>
                {this.renderArrayValues(conjugation.adverbs)}
            </div>
        );
    }
}