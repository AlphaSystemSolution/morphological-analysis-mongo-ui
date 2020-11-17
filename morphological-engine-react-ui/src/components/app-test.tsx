// import Emitter from '../services/event-emitter';
import { Button } from 'primereact/button';
import * as React from 'react';
import InputTable from './input-table';
import { ArabicLetter } from './model/arabic-letter';
import { InputData } from './model/models';
import { NamedTemplate } from './model/named-template';
import { RootLetters } from './model/root-letters';
import { VerbalNoun } from './model/verbal-noun';
import VerbalNounPicker from './verbal-noun-picker';

interface Props { }

interface State {
    initialValues: VerbalNoun[]
    showVerbalNounPicker: boolean
}

export default class AppTest extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);

        this.updateState = this.updateState.bind(this);

        this.state = {
            initialValues: [VerbalNoun.VERBAL_NOUN_FORM_II],
            showVerbalNounPicker: false
        }
    }

    componentDidMount() {
        //Emitter.on('button-clicked', (value: ArabicLabel) => console.log(`Button clicked: ${value.name}`));
        // Emitter.on('letters-selected', (newValue) => console.log(JSON.stringify(newValue)));
    }

    componentWillUnmount() {
        //Emitter.off('button-clicked')
        // Emitter.off('letters-selected');
    }

    private updateState(selectedValues: VerbalNoun[]) {
        this.setState({
            initialValues: selectedValues,
            showVerbalNounPicker: false
        });
    }

    render() {
        const rootLetters: RootLetters = new RootLetters(ArabicLetter.FA, ArabicLetter.TA, ArabicLetter.HHA)
        const data = [new InputData(rootLetters, NamedTemplate.FORM_I_CATEGORY_A_GROUP_A_TEMPLATE), new InputData()]
        return (
            <div>
                <div>
                    <VerbalNounPicker initalValues={this.state.initialValues} showDialog={this.state.showVerbalNounPicker} onHide={this.updateState} />
                    <span className="arabicTitle">Verbal Nouns</span>
                    <div><Button label="Show VerbalNouns" onClick={() => this.setState({ showVerbalNounPicker: true })} aria-haspopup aria-controls="overlay_panel" /></div>
                </div>
                <div>&nbsp;</div>
                <div>
                    <InputTable initialData={data} />
                </div>
            </div>
        );
    }
}