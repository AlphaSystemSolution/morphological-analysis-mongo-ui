import React, { FC, useState } from 'react';
import { observer } from 'mobx-react-lite';
import { ArabicConstants, ExportType, InputData } from './model/models';
import { Checkbox } from 'primereact/checkbox';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Button } from 'primereact/button';
import { Utils } from '../utils/utils';
import { v4 as uuid } from 'uuid';
import { MorphologicalInputForm } from './morphological-input-form';
import { Dialog } from 'primereact/dialog';
import { SplitButton } from 'primereact/splitbutton';
import { Toolbar } from 'primereact/toolbar';
import Project from '../store/project';
import { InputText } from 'primereact/inputtext';
import Emitter from '../services/event-emitter';
import { EmitterConstants } from './emitter-constants';

interface Props {
    project: Project
}

const InputTable: FC<Props> = ({ project }) => {
    const [selectedRows, setSelectedRows] = useState([] as InputData[]);
    const [currentRow, setCurrentRow] = useState(new InputData());
    const [showRowEditDialog, setShowRowEditDialog] = useState(false);
    const [showDeleteRowsDialog, setShowDeleteRowsDialog] = useState(false);
    const [showSaveDialog, setShowSaveDialog] = useState(false);
    const data: InputData[] = project.data.toArray();

    const rowsSelected = (e: any) => setSelectedRows(e.value)

    const rootLettersTemplate = (rowData: InputData) => {
        return <span className="arabicNormal">{rowData.rootLetters.label}</span>
    };

    const familyTemplate = (rowData: InputData) => {
        return (
            <span style={{ 'direction': 'rtl' }}>
                <div style={{ 'textAlign': 'center' }}>{rowData.family.code}</div>
                <div className="arabicNormal" style={{ 'textAlign': 'center' }}>{rowData.family.label}</div>
            </span>
        );
    };

    const verbalNounsTemplate = (rowData: InputData) => {
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
    };

    const removePassiveLineTemplate = (rowData: InputData) => {
        return <Checkbox value={rowData.removePassiveLine} checked={rowData.removePassiveLine} />
    };

    const skipRuleProcessingTemplate = (rowData: InputData) => {
        return <Checkbox value={rowData.skipRuleProcessing} checked={rowData.skipRuleProcessing} />
    };

    const addRow = () => {
        setCurrentRow(new InputData());
        setShowRowEditDialog(true);
    }

    const editRow = (rowData: InputData) => {
        setCurrentRow(rowData);
        setShowRowEditDialog(true);
    };

    const duplicateRow = (rowData: InputData) => {
        const newData = rowData.copy();
        newData.id = uuid();
        editRow(newData);
    };

    const updateRow = (newData?: InputData) => {
        setShowRowEditDialog(false);
        if (newData) {
            const index: number = findIndexById(newData.id);
            project.addData(index, newData);
        }
    };

    const confirmDeleteSelected = () => {
        setShowDeleteRowsDialog(true);
    }

    const deleteSelectedRows = () => {
        setShowDeleteRowsDialog(false);
        const rowsToDelete = selectedRows.map((row) => row.id);
        project.removeData(rowsToDelete)
        setSelectedRows([]);
    };

    const hideDeleteRowsDialog = () => {
        setShowDeleteRowsDialog(false);
    };

    const saveProject = () => {
        if (project.transient) {
            setShowSaveDialog(true);
        } else {
            project.saveProject();
        }
    };

    const handleSaveProjectDialog = (projectName?: string) => {
        setShowSaveDialog(false);
        if (projectName) {
            project.updateProjectName(projectName);
            project.saveProject();
            Emitter.emit(EmitterConstants.PROJECT_SAVED, {});
        }
    };

    const exportToWord = (exportType: ExportType = ExportType.BOTH) => {
        project.exportToWord(exportType, selectedRows).then(() => setSelectedRows([]))
    }

    const findIndexById = (id: string) => {
        let index: number = -1;
        for (let i = 0; i < data.length; i++) {
            if (data[i].id === id) {
                index = i;
                break;
            }
        }
        return index;
    };

    const actionBodyTemplate = (rowData: InputData) => {
        return (
            <>
                <Button type="button" icon="pi pi-pencil" className="p-button-rounded p-button-success" tooltip="Edit" onClick={() => editRow(rowData)} />&nbsp;
                <Button type="button" icon="pi pi-copy" className="p-button-rounded p-button-success" tooltip="Duplicate" onClick={() => duplicateRow(rowData)} />&nbsp;
                <Button type="button" icon="pi pi-info" className="p-button-rounded p-button-secondary" tooltip="Dictionary" onClick={() => Utils.viewDictionary(rowData)} />
            </>
        );
    };

    const exportMenuItems = [
        {
            label: 'Abbreviated Conjugation',
            command: (_: any) => {
                exportToWord(ExportType.ABBREVIATED_CONJUGATION);
            }
        },
        {
            label: 'Detailed Conjugation',
            command: (_: any) => {
                exportToWord(ExportType.DETAILED_CONJUGATION);
            }
        }
    ]

    const leftToolbarContent: any = (
        <>
            <Button label="Add Row" icon="pi pi-plus" className="p-button-success p-mr-2" onClick={addRow} />
            <Button label="Delete Row(s)" icon="pi pi-trash" className="p-button-danger p-mr-2" onClick={confirmDeleteSelected}
                disabled={selectedRows.length <= 0} />
            <Button label="Save" icon="pi pi-save" onClick={saveProject} disabled={data.length <= 0} />
        </>
    );

    const rightToolbarContent: any = (
        <>
            <SplitButton label="Export to Word" icon="pi pi-download" model={exportMenuItems} className="p-md-12" disabled={data.length <= 0}
                onClick={() => exportToWord()} />
        </>
    );

    const deleteRowsDialogFooter = (
        <>
            <Button label="No" icon="pi pi-times" className="p-button-text" onClick={hideDeleteRowsDialog} />
            <Button label="Yes" icon="pi pi-check" className="p-button-text" onClick={deleteSelectedRows} />
        </>
    );

    return (
        <>
            <MorphologicalInputForm inputData={currentRow} visible={showRowEditDialog} onHide={(newData) => updateRow(newData)} />
            <Toolbar left={leftToolbarContent} right={rightToolbarContent} />
            <DataTable value={data} className="p-datatable-gridlines" style={{ 'paddingTop': '0', 'paddingBottom': '0' }} selection={selectedRows}
                onSelectionChange={rowsSelected}>
                <Column selectionMode="multiple" headerStyle={{ width: '3rem' }}></Column>
                <Column field="rootLetters" body={rootLettersTemplate} header="Root Letters" />
                <Column field="family" body={familyTemplate} header="Family" />
                <Column field="translation" header="Translation" />
                <Column field="verbalNouns" header="Verbal Nouns" body={verbalNounsTemplate} />
                <Column field="removePassiveLine" body={removePassiveLineTemplate} header="Remove Passive Line" style={{ width: '10%' }} />
                <Column field="skipRuleProcessing" body={skipRuleProcessingTemplate} header="Skip Rule Processing" style={{ width: '10%' }} />
                <Column body={actionBodyTemplate} headerStyle={{ width: '10em', textAlign: 'center' }} bodyStyle={{ textAlign: 'center', overflow: 'visible' }} />
            </DataTable>
            <Dialog visible={showDeleteRowsDialog} style={{ width: '450px' }} header="Confirm" modal footer={deleteRowsDialogFooter} onHide={hideDeleteRowsDialog}>
                <div className="confirmation-content">
                    <i className="pi pi-exclamation-triangle p-mr-3" style={{ fontSize: '2rem' }} />
                    {<span>Are you sure you want to delete the selected rows?</span>}
                </div>
            </Dialog>
            <SaveFileDialog initialName={project.projectName} showDialog={showSaveDialog} onHide={(projectName?: string) => handleSaveProjectDialog(projectName)} />
        </>
    );
};

interface SaveFileProps {
    initialName: string;
    showDialog: boolean;
    onHide(projectName?: string): void
}

const SaveFileDialog: FC<SaveFileProps> = ({ initialName, showDialog, onHide }) => {

    const [projectName, setProjectName] = useState(initialName);

    const handleFormSubmit = (cancel: boolean = true) => {
        if (cancel) {
            onHide();
        } else {
            onHide(projectName);
        }
    }

    const dialogFooter = (
        <>
            <Button label="Cancel" icon="pi pi-times" className="p-button-text" onClick={() => handleFormSubmit()} />
            <Button label="OK" icon="pi pi-check" className="p-button-text" onClick={() => handleFormSubmit(false)} />
        </>
    );



    return (
        <Dialog visible={showDialog} style={{ width: '450px' }} header="Confirm" footer={dialogFooter} onHide={handleFormSubmit}>
            <div className="p-field p-fluid">
                <label htmlFor="saveproject">Username</label>
                <InputText id="saveproject" type="saveproject" value={projectName} aria-describedby="saveproject-help"
                    onChange={(e: any) => setProjectName(e.target.value)} />
                <small id="saveproject-help">Please enter the name of the projct.</small>
            </div>
        </Dialog>
    );
};

export default observer(InputTable);
