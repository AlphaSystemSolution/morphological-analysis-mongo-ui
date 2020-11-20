import { TabPanel, TabView } from 'primereact/tabview';
import * as React from 'react';
import InputTable from './input-table';
import { InputData, Project } from './model/models';
import { MorphologicalChart } from './model/morphological-chart';

interface Props {
    project: Project,
    chart?: MorphologicalChart
}

interface State { }

export class ProjectView extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);

        this.state = {
            project: new Project("Untitled1", "Untitled1.json"),
            chart: new MorphologicalChart()
        };
    }

    render() {
        return (
            <TabView>
                <TabPanel header="Table">
                    <InputTable initialData={this.props.project!.data} />
                </TabPanel>
                <TabPanel header="Conjugation" disabled>

                </TabPanel>
            </TabView>
        );
    }
}