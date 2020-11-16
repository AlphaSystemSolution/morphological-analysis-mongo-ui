import * as React from 'react';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import { InputText } from 'primereact/inputtext';
import { Dropdown } from 'primereact/dropdown';
import ArabicKeyboard from './arabic-keyboard';
import { InputData } from './model/models';
import { RootLetters } from './model/root-letters';
import { NamedTemplate } from './model/named-template';

interface Props {
    inputData: InputData
    visible?: boolean
    onHide?(result: InputData): void
}

interface State {
    inputData: InputData
    prevData: InputData
    visible: boolean
}

export default class MorphologicalInputForm extends React.Component<Props, State> {

    keyboardRef: any = React.createRef();

    constructor(props: Props) {
        super(props);

        this.familyTemplate = this.familyTemplate.bind(this);
        this.updateFamily = this.updateFamily.bind(this);
        this.state = {
            inputData: this.props.inputData,
            prevData: this.props.inputData,
            visible: this.props.visible ? this.props.visible : false
        }
    }

    public show(newData?: InputData) {
        this.setState({
            inputData: newData ? newData : this.state.inputData,
            visible: true
        });
    }

    private familyTemplate(family: NamedTemplate) {
        return (
            <span style={{ 'direction': 'rtl' }}>
                <span style={{ 'textAlign': 'center' }}>{family.code}&nbsp;&mdash;&nbsp;<span className="arabicNormal">{family.label}</span></span>
            </span>
        );
    }

    private restore(restore: boolean = true) {
        if (restore) {
            this.setState({
                inputData: this.state.prevData,
                visible: false
            });
        } else {
            this.setState({
                visible: false
            }, () => {
                if (this.props.onHide) {
                    this.props.onHide(this.state.inputData);
                }
            });
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
                <Dialog header="Add / Update Morphological Chart Input" footer={footer} onHide={() => this.noop()} closeOnEscape={false} closable={false}
                    visible={this.state.visible}>
                    <div className="p-fluid p-formgrid p-grid">
                        <div className="p-field p-col">
                            <label htmlFor="rootLetters">Root Letters:</label>
                            <InputText id="rootLetters" type="text" value={this.state.inputData.rootLetters.label} className="arabicNormal"
                                onClick={(e) => this.keyboardRef.show(this.state.inputData.rootLetters)} />
                        </div>
                    </div>
                    <div className="p-fluid p-formgrid p-grid">
                        <div className="p-field p-col">
                            <label htmlFor="family">Family</label>
                            <Dropdown id="family" value={this.state.inputData.family} options={NamedTemplate.namedTemplates} onChange={(e) => this.updateFamily(e.value)}
                                valueTemplate={this.familyTemplate} itemTemplate={this.familyTemplate} scrollHeight="600px" />
                        </div>
                    </div>
                    <div>&nbsp;</div>
                    <div>&nbsp;</div>
                    <div>&nbsp;</div>
                    <div>&nbsp;</div>
                </Dialog>
            </React.Fragment>
        );
    }

}