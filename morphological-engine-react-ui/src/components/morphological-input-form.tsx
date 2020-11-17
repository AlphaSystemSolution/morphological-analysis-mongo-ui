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

interface Props {
    inputData: InputData
    visible: boolean
    onHide(result?: InputData): void
}

interface State {
    inputData: InputData
    prevData: InputData
    showVerbalNounPicker: boolean
}

export default class MorphologicalInputForm extends React.Component<Props, State> {

    keyboardRef: any = React.createRef();

    constructor(props: Props) {
        super(props);

        this.familyTemplate = this.familyTemplate.bind(this);
        this.verbalNounTemplate = this.verbalNounTemplate.bind(this);
        this.updateFamily = this.updateFamily.bind(this);
        this.updateTranslation = this.updateTranslation.bind(this);
        this.updateVernalNouns = this.updateVernalNouns.bind(this);
        this.updateRemovePassiveLine = this.updateRemovePassiveLine.bind(this);
        this.updateSkipRuleProcessing = this.updateSkipRuleProcessing.bind(this);
        this.show = this.show.bind(this);

        this.state = {
            inputData: this.props.inputData,
            prevData: this.props.inputData,
            showVerbalNounPicker: false
        }
    }

    private show() {
        this.setState({
            inputData: this.props.inputData
        });
    }

    private familyTemplate(family: NamedTemplate) {
        return (
            <span style={{ 'direction': 'rtl' }}>
                <span style={{ 'textAlign': 'center' }}>{family.code}&nbsp;&mdash;&nbsp;<span className="arabicNormal">{family.label}</span></span>
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
                inputData: this.state.prevData,
            }, () => this.props.onHide());
        } else {
            this.setState({
            }, () => this.props.onHide(this.state.inputData));
        }
    }

    private noop() { }

    private updateRootLetters(rootLetters: RootLetters) {
        const data = this.state.inputData;
        data.rootLetters = rootLetters;
        this.setState({
            inputData: data
        });
    }

    private updateFamily(family: NamedTemplate) {
        const data = this.state.inputData;
        data.family = family;
        data.verbalNouns = VerbalNoun.getByTemplate(family);
        this.setState({
            inputData: data
        });
    }

    private updateTranslation(event: any) {
        const data = this.state.inputData;
        data.translation = event.target.value;
        this.setState({
            inputData: data
        });
    }

    private updateVernalNouns(selectedValues: VerbalNoun[]) {
        const data = this.state.inputData;
        data.verbalNouns = selectedValues;
        this.setState({
            inputData: data,
            showVerbalNounPicker: false
        });
    }

    private updateRemovePassiveLine(event: any) {
        console.log(`${event.checked}`)
        const data = this.state.inputData;
        data.removePassiveLine = event.checked;
        this.setState({
            inputData: data
        });
    }

    private updateSkipRuleProcessing(event: any) {
        const data = this.state.inputData;
        data.skipRuleProcessing = event.checked;
        this.setState({
            inputData: data
        });
    }

    render() {
        const footer = (
            <div>
                <Button label="Cancel" onClick={() => this.restore()} className="p-button-text" />
                <Button label="Save" onClick={() => this.restore(false)} autoFocus />
            </div>
        );
        return (
            <React.Fragment>
                <ArabicKeyboard ref={(el) => this.keyboardRef = el} onHide={(rootLetters) => this.updateRootLetters(rootLetters)} />
                <VerbalNounPicker initalValues={this.state.inputData.verbalNouns} showDialog={this.state.showVerbalNounPicker} onHide={this.updateVernalNouns} />
                <Dialog header="Add / Update Morphological Chart Input" footer={footer} onHide={() => this.noop()} closeOnEscape={false} closable={false}
                    visible={this.props.visible} onShow={this.show}>
                    <div className="p-fluid p-formgrid p-grid">
                        <div className="p-field p-col-12">
                            <label htmlFor="rootLetters" style={{ 'fontWeight': 'bold' }}>Root Letters:</label>
                            <InputText id="rootLetters" type="text" value={this.state.inputData.rootLetters.label} className="arabicNormal"
                                onClick={() => this.keyboardRef.show(this.state.inputData.rootLetters)} />
                        </div>
                    </div>
                    <div className="p-fluid p-formgrid p-grid">
                        <div className="p-field p-col-12">
                            <label htmlFor="family" style={{ 'fontWeight': 'bold' }}>Family:</label>
                            <Dropdown id="family" value={this.state.inputData.family} options={NamedTemplate.namedTemplates} onChange={(e) => this.updateFamily(e.value)}
                                valueTemplate={this.familyTemplate} itemTemplate={this.familyTemplate} scrollHeight="600px" className="multiselect" />
                        </div>
                    </div>
                    <div className="p-fluid p-formgrid p-grid">
                        <div className="p-field p-col-12">
                            <label htmlFor="translation" style={{ 'fontWeight': 'bold' }}>Transalation:</label>
                            <InputText id="translation" type="text" value={this.state.inputData.translation} className="translation"
                                onChange={this.updateTranslation} />
                        </div>
                    </div>
                    <div className="p-fluid p-formgrid p-grid">
                        <div className="p-field p-col-12">
                            <label htmlFor="verbalNouns" style={{ 'fontWeight': 'bold' }}>Verbal Nouns:</label>
                            <Chips id="verbalNouns" value={this.state.inputData.verbalNouns} itemTemplate={this.verbalNounTemplate} disabled={true} max={3}
                                style={{ 'direction': 'rtl' }} /><span>&nbsp;</span>
                            <Button type="button" icon="pi pi-pencil" tooltip="Add / Remove Verbal Nouns" onClick={() => this.setState({ showVerbalNounPicker: true })} />
                        </div>
                    </div>
                    <div className="p-formgroup-inline">
                        <div className="p-field-checkbox">
                            <Checkbox inputId="removePassiveLine" value={this.state.inputData.removePassiveLine} checked={this.state.inputData.removePassiveLine}
                                onChange={this.updateRemovePassiveLine} />
                            <label htmlFor="removePassiveLine" className="p-checkbox-label" style={{ 'fontWeight': 'bold' }}>Remove Passive Line</label>
                        </div>
                        <div className="p-field-checkbox">
                            <Checkbox inputId="skipRuleProcessing" value={this.state.inputData.skipRuleProcessing} checked={this.state.inputData.skipRuleProcessing}
                                onChange={this.updateSkipRuleProcessing} />
                            <label htmlFor="skipRuleProcessing" className="p-checkbox-label" style={{ 'fontWeight': 'bold' }}>Skip Rule Processing</label>
                        </div>
                    </div>
                </Dialog>
            </React.Fragment>
        );
    }

}