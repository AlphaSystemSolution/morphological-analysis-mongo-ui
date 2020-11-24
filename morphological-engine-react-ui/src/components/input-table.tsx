import * as React from 'react';
import { DataTable } from 'primereact/datatable';
import { Checkbox } from 'primereact/checkbox';
import { Column } from 'primereact/column';
import { Button } from 'primereact/button';
import { SplitButton } from 'primereact/splitbutton';
import { ArabicConstants, InputData } from './model/models';
import { MorphologicalInputForm } from './morphological-input-form'
import { IdGenerator } from '../utils/id-generator';
import { Dialog } from 'primereact/dialog';
import { Toolbar } from 'primereact/toolbar';
import { Utils } from '../utils/utils';
import { EmitterConstants } from './emitter-constants';
import Emitter from '../services/event-emitter';
import { ApplicationController } from '../services/application-controller';
import { ConjugationTemplate } from './model/conjugation-template';

interface Props {
    initialData?: InputData[]
}

interface State {
    data: InputData[],
    exportMenuItems: any[],
    selectedRows: InputData[]
    currentRow: InputData
    showRowEditDialog: boolean
    showDeleteRowsDialog: boolean
}

export default class InputTable extends React.Component<Props, State> {

    private applicationController: ApplicationController;

    constructor(props: Props) {
        super(props);

        this.applicationController = new ApplicationController();

        this.rootLettersTemplate = this.rootLettersTemplate.bind(this);
        this.familyTemplate = this.familyTemplate.bind(this);
        this.removePassiveLineTemplate = this.removePassiveLineTemplate.bind(this);
        this.skipRuleProcessingTemplate = this.skipRuleProcessingTemplate.bind(this);
        this.verbalNounsTemplate = this.verbalNounsTemplate.bind(this);
        this.actionBodyTemplate = this.actionBodyTemplate.bind(this);
        this.addRow = this.addRow.bind(this);
        this.editRow = this.editRow.bind(this);
        this.duplicateRow = this.duplicateRow.bind(this);
        this.confirmDeleteSelected = this.confirmDeleteSelected.bind(this);
        this.deleteSelectedRows = this.deleteSelectedRows.bind(this);
        this.hideDeleteRowsDialog = this.hideDeleteRowsDialog.bind(this);
        this.exportToWord = this.exportToWord.bind(this);
        Utils.viewDictionary = Utils.viewDictionary.bind(this);

        this.state = {
            data: this.props.initialData ? this.props.initialData : [],
            exportMenuItems: [
                {
                    label: 'Abbreviated Conjugation',
                    command: (e: any) => {
                        console.log('Export Abbreviated Conjugation to Word');
                    }
                },
                {
                    label: 'Detailed Conjugation',
                    command: (e: any) => {
                        console.log('Export Detailed Conjugation to Word');
                    }
                }
            ],
            selectedRows: [],
            currentRow: new InputData(),
            showRowEditDialog: false,
            showDeleteRowsDialog: false
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
                <Button type="button" icon="pi pi-info" className="p-button-rounded p-button-secondary" tooltip="Dictionary" onClick={() => Utils.viewDictionary(rowData)} />
            </React.Fragment>
        );
    }

    private addRow() {
        this.setState({
            currentRow: new InputData(),
            showRowEditDialog: true
        });
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
        this.setState({
            currentRow: newData,
            showRowEditDialog: true
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
            let action: string
            if (index > -1) {
                data[index] = newData;
                action = EmitterConstants.ROW_UPDATED;
            } else {
                // add new row
                data.push(newData);
                action = EmitterConstants.ROW_UPDATED;
            }
            this.setState({
                showRowEditDialog: false,
                data: data
            }, () => {
                Emitter.emit(action, newData); // emit new data
            });
        }
    }

    private confirmDeleteSelected() {
        this.setState({
            showDeleteRowsDialog: true
        });
    }

    private deleteSelectedRows() {
        const selectedRows = this.state.selectedRows.map((row) => row.id);
        const data = this.state.data.filter((row) => !selectedRows.includes(row.id));
        this.setState({
            data: data,
            selectedRows: [],
            showDeleteRowsDialog: false
        }, () => {
            Emitter.emit(EmitterConstants.ROWS_DELETED, selectedRows)
        });
    }

    private hideDeleteRowsDialog() {
        this.setState({
            showDeleteRowsDialog: false
        });
    }

    private exportToWord() {
        let data = this.state.selectedRows;
        if (data.length <= 0) {
            data = this.state.data;
        }
        const conjugationData = data.map((inputData) => inputData.toConjugationData());
        this.applicationController
            .exportToWord(new ConjugationTemplate(conjugationData))
            .then((_) => {
                this.setState({
                    selectedRows: []
                });
            })
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
        const leftToolbarContent: any = (
            <React.Fragment>
                <Button label="Add Row" icon="pi pi-plus" className="p-button-success p-mr-2" onClick={this.addRow} />
                <Button label="Delete Row(s)" icon="pi pi-trash" className="p-button-danger p-mr-2" onClick={this.confirmDeleteSelected}
                    disabled={this.state.selectedRows.length <= 0} />
            </React.Fragment>
        );

        const rightToolbarContent: any = (
            <React.Fragment>
                <SplitButton label="Export to Word" icon="pi pi-download" model={this.state.exportMenuItems} className="p-md-12" disabled={this.state.data.length <= 0}
                    onClick={this.exportToWord} />
            </React.Fragment >
        );

        const deleteRowsDialogFooter = (
            <React.Fragment>
                <Button label="No" icon="pi pi-times" className="p-button-text" onClick={this.hideDeleteRowsDialog} />
                <Button label="Yes" icon="pi pi-check" className="p-button-text" onClick={this.deleteSelectedRows} />
            </React.Fragment>
        );

        return (
            <React.Fragment>
                <MorphologicalInputForm inputData={this.state.currentRow} visible={this.state.showRowEditDialog} onHide={(newData) => this.updateRow(newData)} />
                <Toolbar left={leftToolbarContent} right={rightToolbarContent} />
                <DataTable value={this.state.data} className="p-datatable-gridlines" style={{ 'paddingTop': '0', 'paddingBottom': '0' }} selection={this.state.selectedRows}
                    onSelectionChange={(e) => this.setState({ selectedRows: e.value })}>
                    <Column selectionMode="multiple" headerStyle={{ width: '3rem' }}></Column>
                    <Column field="rootLetters" body={this.rootLettersTemplate} header="Root Letters" />
                    <Column field="family" body={this.familyTemplate} header="Family" />
                    <Column field="translation" header="Translation" />
                    <Column field="verbalNouns" header="Verbal Nouns" body={this.verbalNounsTemplate} />
                    <Column field="removePassiveLine" body={this.removePassiveLineTemplate} header="Remove Passive Line" style={{ width: '10%' }} />
                    <Column field="skipRuleProcessing" body={this.skipRuleProcessingTemplate} header="Skip Rule Processing" style={{ width: '10%' }} />
                    <Column body={this.actionBodyTemplate} headerStyle={{ width: '10em', textAlign: 'center' }} bodyStyle={{ textAlign: 'center', overflow: 'visible' }} />
                </DataTable>
                <Dialog visible={this.state.showDeleteRowsDialog} style={{ width: '450px' }} header="Confirm" modal footer={deleteRowsDialogFooter} onHide={this.hideDeleteRowsDialog}>
                    <div className="confirmation-content">
                        <i className="pi pi-exclamation-triangle p-mr-3" style={{ fontSize: '2rem' }} />
                        {<span>Are you sure you want to delete the selected rows?</span>}
                    </div>
                </Dialog>
            </React.Fragment>
        );
    }
}