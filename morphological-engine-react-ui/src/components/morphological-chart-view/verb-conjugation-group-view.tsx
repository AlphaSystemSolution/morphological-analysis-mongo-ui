import * as React from 'react';
import { VerbConjugationGroup } from '../model/detailed-conjugation';
import { Panel } from 'primereact/panel';
import { ConjugationTupleView } from './conjugation-tuple.view';

interface Props {
    group: VerbConjugationGroup
}

interface State { }

export class VerbConjugationGroupView extends React.Component<Props, State> {

    render() {
        const group = this.props.group;
        return (
            <Panel header={group.termTypeValue.label} style={{ direction: 'rtl' }}>
                <div className="p-grid arabicNormal">
                    <ConjugationTupleView tuple={group.masculineThirdPerson} />
                    <ConjugationTupleView tuple={group.feminineThirdPerson} />
                    <ConjugationTupleView tuple={group.masculineSecondPerson} />
                    <ConjugationTupleView tuple={group.feminineSecondPerson} />
                    <ConjugationTupleView tuple={group.firstPerson} />
                </div>
            </Panel>
        );
    }
}