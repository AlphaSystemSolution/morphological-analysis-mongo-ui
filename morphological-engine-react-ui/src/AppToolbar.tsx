import * as React from 'react';

import { Toolbar } from 'primereact/toolbar';
import { Button } from 'primereact/button';
// import { SplitButton } from 'primereact/splitbutton';
import Emitter from './services/event-emitter';

interface Props { }

interface State { }

export default class AppToolbar extends React.Component<Props, State> {
    constructor(props: Props) {
        super(props);

        this.createNewProject = this.createNewProject.bind(this);

        this.state = {};
    }

    private createNewProject() {
        Emitter.emit('create-new-project', {})
    }

    render() {
        const leftContents: any = (
            <React.Fragment>
                <Button label="New" icon="pi pi-plus" className="p-mr-2" onClick={this.createNewProject} />
                <span>&nbsp;</span>
                <Button label="Open" icon="pi pi-file-o" className="p-mr-2" />
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
