import * as React from 'react';

import { Toolbar } from 'primereact/toolbar';
import { Button } from 'primereact/button';
// import { SplitButton } from 'primereact/splitbutton';

export default class AppToolbar extends React.Component {

    render() {
        const leftContents: any = (
            <React.Fragment>
                <Button label="New" icon="pi pi-plus" className="p-mr-2"/>
                <span>&nbsp;</span>
                <Button label="Open" icon="pi pi-file-o" className="p-mr-2"/>
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
