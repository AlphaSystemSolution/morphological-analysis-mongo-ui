import * as React from 'react';
import { DataTable } from 'primereact/datatable';
import { Checkbox } from 'primereact/checkbox';
import { Column } from 'primereact/column';
import { Button } from 'primereact/button';
import { ArabicConstants, InputData } from './model/models';
import MorphologicalInputForm from './morphological-input-form'
import { IdGenerator } from '../utils/id-generator';

interface Props {
    initialData?: InputData[]
}

interface State {
    data: InputData[],
    selectedRows: InputData[]
    currentRow: InputData
    showRowEditDialog: boolean
}

export default class InputTable extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);

        this.rootLettersTemplate = this.rootLettersTemplate.bind(this);
        this.familyTemplate = this.familyTemplate.bind(this);
        this.removePassiveLineTemplate = this.removePassiveLineTemplate.bind(this);
        this.skipRuleProcessingTemplate = this.skipRuleProcessingTemplate.bind(this);
        this.verbalNounsTemplate = this.verbalNounsTemplate.bind(this);
        this.actionBodyTemplate = this.actionBodyTemplate.bind(this);
        this.editRow = this.editRow.bind(this);
        this.duplicateRow = this.duplicateRow.bind(this);

        this.state = {
            data: this.props.initialData ? this.props.initialData : [],
            selectedRows: [],
            currentRow: new InputData(),
            showRowEditDialog: false
        }
    }

    private rootLettersTemplate(rowData: InputData) {
        return <span className="arabicNormal">{rowData.rootLetters.label}</span>
    }

    private familyTemplate(rowData: InputData) {
        return (
            <span style={{ 'direction': 'rtl' }}>
                <div style={{ 'textAlign': 'center' }}>{rowData.family.code}</div>
                <div className="arabicNormal" style={{ 'textAlign': 'center' }}>{rowData.family.label}</div>
            </span>
        );
    }

    private verbalNounsTemplate(rowData: InputData) {
        const verbalNouns = rowData.verbalNouns;
        if (verbalNouns.length <= 0) {
            return <span>&nbsp;</span>
        } else {
            const array = [<span className="arabicNormal" key="vn-1">{verbalNouns[0].label}</span>];
            if (verbalNouns.length >= 2) {
                array.push(<span className="arabicDisabled" key="vn-2">{ArabicConstants.AND_SPACE.label}</span>);
                array.push(<span className="arabicNormal" key="vn-3">{verbalNouns[1].label}</span>);
            }
            if (array.length >= 3) {
                array.unshift(<span className="arabicDisabled" key="vn-4"> ... </span>)
            }
            return (
                <span className="arabicNormal" style={{ direction: 'rtl', textAlign: 'right' }}>
                    {array}
                </span>
            );
        }
    }

    removePassiveLineTemplate(rowData: InputData) {
        return <Checkbox value={rowData.removePassiveLine} checked={rowData.removePassiveLine} />
    }

    skipRuleProcessingTemplate(rowData: InputData) {
        return <Checkbox value={rowData.skipRuleProcessing} checked={rowData.skipRuleProcessing} />
    }

    actionBodyTemplate(rowData: InputData) {
        return (
            <React.Fragment>
                <Button type="button" icon="pi pi-pencil" className="p-button-secondary" tooltip="Edit" onClick={() => this.editRow(rowData)} />&nbsp;
                <Button type="button" icon="pi pi-copy" className="p-button-secondary" tooltip="Duplicate" onClick={() => this.duplicateRow(rowData)} />
            </React.Fragment>
        );
    }

    private editRow(rowData: InputData) {
        this.setState({
            currentRow: rowData,
            showRowEditDialog: true
        });
    }

    private duplicateRow(rowData: InputData) {
        const newData = rowData.copy();
        newData.id = IdGenerator.nextId();
        this.state.data.push(newData);
        this.setState({
            data: this.state.data
        });
    }

    private updateRow(newData?: InputData) {
        if (!newData) {
            this.setState({
                showRowEditDialog: false
            });
        } else {
            const index: number = this.findIndexById(newData.id);
            let data = this.state.data;
            if (index > -1) {
                data[index] = newData;
            } else {
                // add new row
                data.push(newData);
            }
            this.setState({
                showRowEditDialog: false,
                data: data
            });
        }

    }

    private findIndexById(id: string): number {
        let index: number = -1;
        for (let i = 0; i < this.state.data.length; i++) {
            if (this.state.data[i].id === id) {
                index = i;
                break;
            }
        }
        return index;
    }

    render() {
        return (
            <React.Fragment>
                <MorphologicalInputForm inputData={this.state.currentRow} visible={this.state.showRowEditDialog} onHide={(newData) => this.updateRow(newData)} />
                <DataTable value={this.state.data} className="p-datatable-gridlines" style={{ 'paddingTop': '0', 'paddingBottom': '0' }} selection={this.state.selectedRows}
                    onSelectionChange={(e) => this.setState({ selectedRows: e.value })} editMode="cell">
                    <Column selectionMode="multiple" headerStyle={{ width: '3rem' }}></Column>
                    <Column field="rootLetters" body={this.rootLettersTemplate} header="Root Letters" />
                    <Column field="family" body={this.familyTemplate} header="Family" />
                    <Column field="translation" header="Translation" />
                    <Column field="verbalNouns" header="Verbal Nouns" body={this.verbalNounsTemplate} />
                    <Column field="removePassiveLine" body={this.removePassiveLineTemplate} header="Remove Passive Line" style={{ width: '10%' }} />
                    <Column field="skipRuleProcessing" body={this.skipRuleProcessingTemplate} header="Skip Rule Processing" style={{ width: '10%' }} />
                    <Column body={this.actionBodyTemplate} headerStyle={{ width: '8em', textAlign: 'center' }} bodyStyle={{ textAlign: 'center', overflow: 'visible' }} />
                </DataTable>
            </React.Fragment>
        );
    }
}