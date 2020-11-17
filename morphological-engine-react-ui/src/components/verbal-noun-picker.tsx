import * as React from 'react';
import { Button } from 'primereact/button';
import { Dialog } from 'primereact/dialog';
import { VerbalNoun } from './model/verbal-noun';
import ToggleSelecter from './toggle-selecter';
import Emitter from '../services/event-emitter';

interface Props {
    initalValues: VerbalNoun[]
    showDialog: boolean
    onHide(verbalNouns: VerbalNoun[]): void
}

interface State {
    selectedValues: VerbalNoun[]
    currentlySelectedValues: VerbalNoun[]
}

export default class VerbalNounPicker extends React.Component<Props, State> {

    private availableValues: VerbalNoun[][] = [
        [VerbalNoun.VERBAL_NOUN_V5, VerbalNoun.VERBAL_NOUN_V4, VerbalNoun.VERBAL_NOUN_V3, VerbalNoun.VERBAL_NOUN_V2, VerbalNoun.VERBAL_NOUN_V1],
        [VerbalNoun.VERBAL_NOUN_V10, VerbalNoun.VERBAL_NOUN_V9, VerbalNoun.VERBAL_NOUN_V8, VerbalNoun.VERBAL_NOUN_V7, VerbalNoun.VERBAL_NOUN_V6,],
        [VerbalNoun.VERBAL_NOUN_V15, VerbalNoun.VERBAL_NOUN_V14, VerbalNoun.VERBAL_NOUN_V13, VerbalNoun.VERBAL_NOUN_V12, VerbalNoun.VERBAL_NOUN_V11],
        [VerbalNoun.VERBAL_NOUN_V20, VerbalNoun.VERBAL_NOUN_V19, VerbalNoun.VERBAL_NOUN_V18, VerbalNoun.VERBAL_NOUN_V17, VerbalNoun.VERBAL_NOUN_V16],
        [VerbalNoun.VERBAL_NOUN_V25, VerbalNoun.VERBAL_NOUN_V24, VerbalNoun.VERBAL_NOUN_V23, VerbalNoun.VERBAL_NOUN_V22, VerbalNoun.VERBAL_NOUN_V21],
        [VerbalNoun.VERBAL_NOUN_FORM_II_DEFECTIVE_VERB, VerbalNoun.VERBAL_NOUN_FORM_II, VerbalNoun.VERBAL_NOUN_V28, VerbalNoun.VERBAL_NOUN_V27, VerbalNoun.VERBAL_NOUN_V26],
        [VerbalNoun.VERBAL_NOUN_FORM_V, VerbalNoun.VERBAL_NOUN_FORM_IV, VerbalNoun.VERBAL_NOUN_FORM_III_DEFECTIVE_VERB, VerbalNoun.VERBAL_NOUN_FORM_III_V2, VerbalNoun.VERBAL_NOUN_FORM_III_V1],
        [VerbalNoun.VERBAL_NOUN_FORM_X, VerbalNoun.VERBAL_NOUN_FORM_IX, VerbalNoun.VERBAL_NOUN_FORM_VIII, VerbalNoun.VERBAL_NOUN_FORM_VII, VerbalNoun.VERBAL_NOUN_FORM_VI]
    ];

    constructor(props: Props) {
        super(props);

        this.show = this.show.bind(this);

        this.state = {
            selectedValues: this.copyArray(this.props.initalValues),
            currentlySelectedValues: this.copyArray(this.props.initalValues)
        };
    }

    componentDidMount() {
        Emitter.on('toggle-state-changed', (newValue) => this.valueSelected(newValue));
    }

    componentWillUnmount() {
        Emitter.off('toggle-state-changed');
    }

    private copyArray(array: VerbalNoun[]) {
        return array.map(x => Object.assign({}, x));
    }

    private show() {
        this.setState({
            selectedValues: this.copyArray(this.props.initalValues),
            currentlySelectedValues: this.copyArray(this.props.initalValues)
        });
    }

    private valueSelected(payload: any) {
        const verbalNoun = payload.value;
        const selected = payload.selected;
        let updatedValues: VerbalNoun[] = this.copyArray(this.state.currentlySelectedValues);
        if (selected) {
            updatedValues.push(verbalNoun);
        } else {
            updatedValues = updatedValues.filter((e) => !verbalNoun.equals(e));
        }
        this.setState({
            currentlySelectedValues: updatedValues
        });
    }

    private dialogSubmitted(cancelled: boolean = true) {
        if (cancelled) {
            this.setState({
                currentlySelectedValues: this.copyArray(this.state.selectedValues)
            }, () => this.props.onHide(this.state.selectedValues));
        } else {
            this.setState({
                selectedValues: this.copyArray(this.state.currentlySelectedValues)
            }, () => this.props.onHide(this.state.selectedValues));
        }
    }

    private noop() { }

    private isSelected(verbalNoun: VerbalNoun): boolean {
        return this.state.currentlySelectedValues.filter((value) => value.code === verbalNoun.code).length > 0;
    }

    private renderAllValues() {
        const values = this.availableValues.flatMap((values, index) => this.renderSingleRow(values, index));
        return <div>{values}</div>
    }

    private renderSingleRow(verbalNouns: VerbalNoun[], parentIndex: number) {
        const values =
            verbalNouns.map((verbalNoun, index) => {
                const i = (10 * parentIndex) + index;
                return (
                    <span>
                        <ToggleSelecter key={i} value={verbalNoun} className="verbalNounToggleSelector ui-button p-button-raised"
                            selected={this.isSelected(verbalNoun)} editable={true} index={i} />&nbsp;
                    </span>
                );
            });
        return <div>{values}</div>;
    }

    render() {
        const footer = (
            <div>
                <Button label="Cancel" onClick={() => this.dialogSubmitted()} className="p-button-text" />
                <Button label="OK" onClick={() => this.dialogSubmitted(false)} autoFocus />
            </div>
        );

        return (
            <Dialog header="Select Verbal Nouns" footer={footer} visible={this.props.showDialog} closeOnEscape={false} closable={false}
                onShow={this.show} onHide={() => this.noop()}>
                {this.renderAllValues()}
            </Dialog>
        );
    }
}