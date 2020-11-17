import * as React from 'react';
import { DataTable } from 'primereact/datatable';
import { Checkbox } from 'primereact/checkbox';
import { Column } from 'primereact/column';
import { Button } from 'primereact/button';
import { ArabicConstants, InputData } from './model/models';
import MorphologicalInputForm from './morphological-input-form'
import { IdGenerator } from '../utils/id-generator';
import { Dialog } from 'primereact/dialog';

interface Props {
    initialData?: InputData[]
}

interface State {
    data: InputData[],
    selectedRows: InputData[]
    currentRow: InputData
    showRowEditDialog: boolean,
    showDeleteRowDialog: boolean
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
        this.deleteRow = this.deleteRow.bind(this);
        this.confirmDeleteRow = this.confirmDeleteRow.bind(this);
        this.hideDeleteRowDialog = this.hideDeleteRowDialog.bind(this);

        this.state = {
            data: this.props.initialData ? this.props.initialData : [],
            selectedRows: [],
            currentRow: new InputData(),
            showRowEditDialog: false,
            showDeleteRowDialog: false
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

    private removePassiveLineTemplate(rowData: InputData) {
        return <Checkbox value={rowData.removePassiveLine} checked={rowData.removePassiveLine} />
    }

    private skipRuleProcessingTemplate(rowData: InputData) {
        return <Checkbox value={rowData.skipRuleProcessing} checked={rowData.skipRuleProcessing} />
    }

    private actionBodyTemplate(rowData: InputData) {
        return (
            <React.Fragment>
                <Button type="button" icon="pi pi-pencil" className="p-button-rounded p-button-success" tooltip="Edit" onClick={() => this.editRow(rowData)} />&nbsp;
                <Button type="button" icon="pi pi-copy" className="p-button-rounded p-button-success" tooltip="Duplicate" onClick={() => this.duplicateRow(rowData)} />&nbsp;
                <Button type="button" icon="pi pi-trash" className="p-button-rounded p-button-warning" tooltip="Duplicate" onClick={() => this.confirmDeleteRow(rowData)} />
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

    private confirmDeleteRow(rowData: InputData){
        this.setState({
            currentRow: rowData,
            showDeleteRowDialog: true
        });
    }

    private deleteRow(){
        const data = this.state.data.filter((current) => this.state.currentRow.id !== current.id);
        this.setState({
            data: data,
            showDeleteRowDialog: false
        });
    }

    private hideDeleteRowDialog() {
        this.setState({
            showDeleteRowDialog: false
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
        const deleteRowDialogFooter = (
            <React.Fragment>
                <Button label="No" icon="pi pi-times" className="p-button-text" onClick={this.hideDeleteRowDialog} />
                <Button label="Yes" icon="pi pi-check" className="p-button-text" onClick={this.deleteRow} />
            </React.Fragment>
        );

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
                    <Column body={this.actionBodyTemplate} headerStyle={{ width: '10em', textAlign: 'center' }} bodyStyle={{ textAlign: 'center', overflow: 'visible' }} />
                </DataTable>
                <Dialog visible={this.state.showDeleteRowDialog} style={{ width: '450px' }} header="Confirm" modal footer={deleteRowDialogFooter} onHide={this.hideDeleteRowDialog}>
                    <div className="confirmation-content">
                        <i className="pi pi-exclamation-triangle p-mr-3" style={{ fontSize: '2rem'}} />
                        {<span>Are you sure you want to delete current row?</span>}
                    </div>
                </Dialog>
            </React.Fragment>
        );
    }
}