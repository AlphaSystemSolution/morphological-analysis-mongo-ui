import * as React from 'react';
import { DataTable } from 'primereact/datatable';
import { Checkbox } from 'primereact/checkbox';
import { Column } from 'primereact/column';
import { Button } from 'primereact/button';
import { InputData } from './model/models';
import MorphologicalInputForm from './morphological-input-form'

interface Props {
    initialData?: InputData[]
}

interface State {
    data: InputData[],
    selectedRows: InputData[]
    currentRow: InputData
    showEditDialog: boolean
}

export default class InputTable extends React.Component<Props, State> {

    dialogRef: any = React.createRef();

    constructor(props: Props) {
        super(props);

        this.rootLettersTemplate = this.rootLettersTemplate.bind(this);
        this.familyTemplate = this.familyTemplate.bind(this);
        this.removePassiveLineTemplate = this.removePassiveLineTemplate.bind(this);
        this.skipRuleProcessingTemplate = this.skipRuleProcessingTemplate.bind(this);
        this.actionBodyTemplate = this.actionBodyTemplate.bind(this);
        this.editRow = this.editRow.bind(this);

        this.state = {
            data: this.props.initialData ? this.props.initialData : [],
            selectedRows: [],
            currentRow: new InputData(),
            showEditDialog: false
        }
    }

    rootLettersTemplate(rowData: InputData) {
        return <span className="arabicNormal">{rowData.rootLetters.label}</span>
    }

    familyTemplate(rowData: InputData) {
        return (
            <span style={{ 'direction': 'rtl' }}>
                <div style={{ 'textAlign': 'center' }}>{rowData.family.code}</div>
                <div className="arabicNormal" style={{ 'textAlign': 'center' }}>{rowData.family.label}</div>
            </span>
        );
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
                <Button type="button" icon="pi pi-copy" className="p-button-secondary" tooltip="Duplicate" />
            </React.Fragment>
        );
    }

    private editRow(rowData: InputData) {
        this.setState({
            currentRow: rowData,
            showEditDialog: true
        }, () => {
            this.dialogRef.show(rowData);
        });
    }

    private updateRow(newData: InputData) {
        const index: number = this.findIndexById(newData.id);
        let data = this.state.data;
        if (index > -1) {
            data[index] = newData;
        } else {
            // add new row
            data.push(newData);
        }
        this.setState({
            showEditDialog: false,
            data: data
        });
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
                <MorphologicalInputForm ref={(el) => this.dialogRef = el} inputData={this.state.currentRow} visible={false}
                    onHide={(newData) => this.updateRow(newData)} />
                <DataTable value={this.state.data} className="p-datatable-gridlines" style={{ 'paddingTop': '0', 'paddingBottom': '0' }} selection={this.state.selectedRows}
                            onSelectionChange={(e) => this.setState({ selectedRows: e.value })} editMode="cell">
                    <Column selectionMode="multiple" headerStyle={{ width: '3rem' }}></Column>
                    <Column field="rootLetters" body={this.rootLettersTemplate} header="Root Letters" />
                    <Column field="family" body={this.familyTemplate} header="Family" />
                    <Column field="translation" header="Translation" />
                    <Column field="removePassiveLine" body={this.removePassiveLineTemplate} header="Remove Passive Line" style={{ width: '10%' }} />
                    <Column field="skipRuleProcessing" body={this.skipRuleProcessingTemplate} header="Skip Rule Processing" style={{ width: '10%' }} />
                    <Column body={this.actionBodyTemplate} headerStyle={{ width: '8em', textAlign: 'center' }} bodyStyle={{ textAlign: 'center', overflow: 'visible' }} />
                </DataTable>
            </React.Fragment>
        );
    }
}