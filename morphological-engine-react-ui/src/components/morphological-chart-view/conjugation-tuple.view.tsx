import * as React from 'react';
import { ConjugationTuple } from '../model/detailed-conjugation';

interface Props {
    tuple?: ConjugationTuple
}

interface State { }

export class ConjugationTupleView extends React.Component<Props, State> {

    render() {
        const tuple = this.props.tuple;
        if (tuple) {
            let dual = null;
            let pluarClassName = "p-col-8 p-md-12 p-lg-8";
            if (tuple.dual) {
                dual = (
                    <div className="p-col-4 p-md-12 p-lg-4">
                        <div className="box">
                            {tuple.dual}
                        </div>
                    </div>
                );
                pluarClassName = "p-col-4 p-md-12 p-lg-4";
            }
            return (
                <React.Fragment>
                    <div className="p-col-4 p-md-12 p-lg-4">
                        <div className="box">
                            {tuple.singular}
                        </div>
                    </div>
                    {dual}
                    <div className={pluarClassName}>
                        <div className="box">
                            {tuple.plural}
                        </div>
                    </div>
                </React.Fragment>
            );
        } else {
            return null;
        }

    }
}