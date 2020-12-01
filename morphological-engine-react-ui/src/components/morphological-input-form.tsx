import * as React from 'react';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import { InputText } from 'primereact/inputtext';
import { Dropdown } from 'primereact/dropdown';
import { Chips } from 'primereact/chips';
import ArabicKeyboard from './arabic-keyboard';
import { InputData } from './model/models';
import { RootLetters } from './model/root-letters';
import { NamedTemplate } from './model/named-template';
import { Checkbox } from 'primereact/checkbox';
import { VerbalNoun } from './model/verbal-noun';
import VerbalNounPicker from './verbal-noun-picker';
import { Utils } from '../utils/utils';

interface Props {
    inputData: InputData
    visible: boolean
    onHide(result?: InputData): void
}

interface State {
    currentData: InputData
    inputData: InputData
    showKeyboard: boolean
    showVerbalNounPicker: boolean
}

export class MorphologicalInputForm extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);

        this.updateRootLetters = this.updateRootLetters.bind(this);
        this.familyTemplate = this.familyTemplate.bind(this);
        this.verbalNounTemplate = this.verbalNounTemplate.bind(this);
        this.updateFamily = this.updateFamily.bind(this);
        this.updateTranslation = this.updateTranslation.bind(this);
        this.updateVernalNouns = this.updateVernalNouns.bind(this);
        this.updateRemovePassiveLine = this.updateRemovePassiveLine.bind(this);
        this.updateSkipRuleProcessing = this.updateSkipRuleProcessing.bind(this);
        this.show = this.show.bind(this);
        this.showKeyboard = this.showKeyboard.bind(this);

        this.state = {
            currentData: this.props.inputData.copy(),
            inputData: this.props.inputData.copy(),
            showKeyboard: false,
            showVerbalNounPicker: false
        }
    }

    private show() {
        this.setState({
            currentData: this.props.inputData.copy(),
            inputData: this.props.inputData.copy()
        });
    }

    private familyTemplate(family: NamedTemplate) {
        return (
            <span style={{ direction: 'rtl', textAlign: 'center' }}>
                {family.code}&nbsp;&mdash;&nbsp;<span className="arabicNormal">{family.label}</span>
            </span>
        );
    }

    private verbalNounTemplate(verbalNoun: VerbalNoun) {
        return (
            <span style={{ 'direction': 'rtl' }}>
                <span className="arabicNormal">{verbalNoun.label}</span>
            </span>
        );
    }

    private restore(restore: boolean = true) {
        if (restore) {
            this.setState({
                inputData: this.state.currentData.copy(),
            }, () => this.props.onHide());
        } else {
            this.setState({
            }, () => this.props.onHide(this.state.currentData.copy()));
        }
    }

    private noop() { }

    private updateRootLetters(rootLetters?: RootLetters) {
        const data = this.state.currentData;
        if (rootLetters) {
            data.rootLetters = rootLetters;
        }
        this.setState({
            currentData: data,
            showKeyboard: false
        });
    }

    private updateFamily(family: NamedTemplate) {
        const data = this.state.currentData;
        data.family = family;
        data.verbalNouns = VerbalNoun.getByTemplate(family);
        this.setState({
            currentData: data
        });
    }

    private updateTranslation(event: any) {
        const data = this.state.currentData;
        data.translation = event.target.value;
        this.setState({
            currentData: data
        });
    }

    private updateVernalNouns(selectedValues: VerbalNoun[]) {
        const data = this.state.currentData;
        data.verbalNouns = selectedValues;
        this.setState({
            currentData: data,
            showVerbalNounPicker: false
        });
    }

    private updateRemovePassiveLine(event: any) {
        const data = this.state.currentData;
        data.removePassiveLine = event.checked;
        this.setState({
            currentData: data
        });
    }

    private updateSkipRuleProcessing(event: any) {
        const data = this.state.currentData;
        data.skipRuleProcessing = event.checked;
        this.setState({
            currentData: data
        });
    }

    private showKeyboard() {
        this.setState({ showKeyboard: true });
    }

    render() {
        const footer = (
            <div>
                <Button label="View Dictionary" className="p-button-text" onClick={() => Utils.viewDictionary(this.state.currentData)} />
                <Button label="Cancel" onClick={() => this.restore()} className="p-button-text" />
                <Button label="Save" onClick={() => this.restore(false)} autoFocus />
            </div>
        );
        return (
            <React.Fragment>
                <ArabicKeyboard onHide={this.updateRootLetters} visible={this.state.showKeyboard} rootLetters={this.state.currentData.rootLetters} />
                <VerbalNounPicker initalValues={this.state.currentData.verbalNouns} showDialog={this.state.showVerbalNounPicker} onHide={this.updateVernalNouns} />
                <Dialog header="Add / Update Morphological Chart Input" footer={footer} onHide={() => this.noop()} closeOnEscape={false} closable={false}
                    visible={this.props.visible} onShow={this.show}>
                    <div className="p-fluid p-formgrid p-grid">
                        <div className="p-field p-col-12">
                            <label htmlFor="rootLetters" style={{ 'fontWeight': 'bold' }}>Root Letters:</label>
                            <InputText id="rootLetters" type="text" value={this.state.currentData.rootLetters.label} className="arabicNormal"
                                onClick={this.showKeyboard} />
                        </div>
                    </div>
                    <div className="p-fluid p-formgrid p-grid">
                        <div className="p-field p-col-12">
                            <label htmlFor="family" style={{ 'fontWeight': 'bold' }}>Family:</label>
                            <Dropdown id="family" value={this.state.currentData.family} options={NamedTemplate.namedTemplates} onChange={(e) => this.updateFamily(e.value)}
                                valueTemplate={this.familyTemplate} itemTemplate={this.familyTemplate} className="multiselect" />
                        </div>
                    </div>
                    <div className="p-fluid p-formgrid p-grid">
                        <div className="p-field p-col-12">
                            <label htmlFor="translation" style={{ 'fontWeight': 'bold' }}>Transalation:</label>
                            <InputText id="translation" type="text" value={this.state.currentData.translation} className="translation-small"
                                onChange={this.updateTranslation} />
                        </div>
                    </div>
                    <div className="p-fluid p-formgrid p-grid">
                        <div className="p-field p-col-12">
                            <label htmlFor="verbalNouns" style={{ 'fontWeight': 'bold' }}>Verbal Nouns:</label>
                            <Chips id="verbalNouns" value={this.state.currentData.verbalNouns} itemTemplate={this.verbalNounTemplate} disabled={true} max={3}
                                style={{ 'direction': 'rtl' }} /><span>&nbsp;</span>
                            <Button type="button" icon="pi pi-pencil" tooltip="Add / Remove Verbal Nouns" onClick={() => this.setState({ showVerbalNounPicker: true })} />
                        </div>
                    </div>
                    <div className="p-formgroup-inline">
                        <div className="p-field-checkbox">
                            <Checkbox inputId="removePassiveLine" value={this.state.currentData.removePassiveLine} checked={this.state.currentData.removePassiveLine}
                                onChange={this.updateRemovePassiveLine} />
                            <label htmlFor="removePassiveLine" className="p-checkbox-label" style={{ 'fontWeight': 'bold' }}>Remove Passive Line</label>
                        </div>
                        <div className="p-field-checkbox">
                            <Checkbox inputId="skipRuleProcessing" value={this.state.currentData.skipRuleProcessing} checked={this.state.currentData.skipRuleProcessing}
                                onChange={this.updateSkipRuleProcessing} />
                            <label htmlFor="skipRuleProcessing" className="p-checkbox-label" style={{ 'fontWeight': 'bold' }}>Skip Rule Processing</label>
                        </div>
                    </div>
                </Dialog>
            </React.Fragment>
        );
    }

}