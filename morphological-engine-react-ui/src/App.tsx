import * as React from 'react';

import 'primereact/resources/themes/saga-green/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';
import 'primeflex/primeflex.css';

import './assets/font.css';
import './assets/App.css';

import { AppToolbar } from './components/AppToolbar';
import { ProjectView } from './components/project-view';

interface Props { }

interface State { }

export default class App extends React.Component<Props, State> {

  constructor(props: Props) {
    super(props);

    this.state = {}
  }

  render() {
    return (
      <div className="App">
        <AppToolbar />
        <div>&nbsp;</div>
        <ProjectView />
      </div>
    );
  }
}
