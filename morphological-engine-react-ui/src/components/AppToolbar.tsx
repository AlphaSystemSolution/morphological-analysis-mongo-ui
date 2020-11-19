import * as React from 'react';

import * as path from 'path';
import { Toolbar } from 'primereact/toolbar';
import { Button } from 'primereact/button';
import { FileUpload } from 'primereact/fileupload';
// import { SplitButton } from 'primereact/splitbutton';
import Emitter from '../services/event-emitter';
import { ConjugationTemplate } from "../components/model/conjugation-template";
import { Project } from './model/models';

interface Props { }

interface State { }

export class AppToolbar extends React.Component<Props, State> {

    static NEW_PROJECT_ACTION: string = "create-new-project";
    static IMPORT_PROJECT_ACTION: string = "import-project"

    fileUploaderRef: any = React.createRef();

    constructor(props: Props) {
        super(props);

        this.uploadHandler = this.uploadHandler.bind(this);

        this.state = {};
    }

    private emitAction(event: string, payload: any = {}) {
        Emitter.emit(event, payload)
    }

    private uploadHandler(event: any) {
        if (event.files) {
            const file = event.files[0];
            const fileReader = new FileReader();
            fileReader.readAsText(file);
            fileReader.onload = () => {
                const content = fileReader.result as string;
                const src = JSON.parse(content);
                const ct = ConjugationTemplate.of(src);
                const data = ct.data.map((d) => d.toInputData())
                const project = new Project(path.basename(file.name, ".json"), file.name, data, ct.chartConfiguration)                
                Emitter.emit(AppToolbar.IMPORT_PROJECT_ACTION, project)
                this.fileUploaderRef.clear();
            };
            fileReader.onerror = () => {
                console.error(`Error reading file: ${file.name}`)
                throw new Error("Unable to read file");
            }
        }
    }

    render() {
        const leftContents: any = (
            <React.Fragment>
                <Button label="New" icon="pi pi-plus" className="p-mr-2" onClick={() => this.emitAction(AppToolbar.NEW_PROJECT_ACTION)} />
                <span>&nbsp;</span>
                <FileUpload ref={(el) => this.fileUploaderRef = el} name="import" accept="*.json" mode="basic" chooseLabel="Import" customUpload uploadHandler={this.uploadHandler}
                    auto={true} />
                <span>&nbsp;</span>
                <i className="pi pi-bars p-toolbar-separator p-mr-2" />
            </React.Fragment>
        );

        return (
            <div className="content-section implementation">
                <Toolbar left={leftContents} />
            </div>
        );
    }
}
