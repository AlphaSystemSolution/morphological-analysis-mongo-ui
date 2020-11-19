import * as React from 'react';

import { Toolbar } from 'primereact/toolbar';
import { Button } from 'primereact/button';
import { FileUpload } from 'primereact/fileupload';
// import { SplitButton } from 'primereact/splitbutton';
import Emitter from '../services/event-emitter';

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

    componentDidMount() {
        Emitter.on("project-created", () => this.fileUploaderRef.clear());
    }

    componentWillUnmount() {
        Emitter.off("project-created");
    }

    private emitAction(event: string, payload: any = {}) {
        Emitter.emit(event, payload)
    }

    private uploadHandler(event: any) {
        if (event.files) {
            this.emitAction(AppToolbar.IMPORT_PROJECT_ACTION, event.files[0]);
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
