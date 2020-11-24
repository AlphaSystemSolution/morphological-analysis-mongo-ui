import * as React from 'react';
import { NounConjugationGroup } from '../model/detailed-conjugation';
import { Panel } from 'primereact/panel';
import { ConjugationTupleView } from './conjugation-tuple.view';

interface Props {
    group: NounConjugationGroup
}

interface State { }

export class NounConjugationGroupView extends React.Component<Props, State> {

    render() {
        const group = this.props.group;
        return (
            <Panel header={group.termTypeValue.label} style={{ direction: 'rtl' }}>
                <div className="p-grid arabicNormal">
                    <ConjugationTupleView tuple={group.nominative} />
                    <ConjugationTupleView tuple={group.accusative} />
                    <ConjugationTupleView tuple={group.genitive} />
                </div>
            </Panel>
        );
    }
}