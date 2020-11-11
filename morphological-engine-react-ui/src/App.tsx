import * as React from 'react';

import 'primereact/resources/themes/saga-green/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';
import 'primeflex/primeflex.css';

import './assets/font.css';
import './assets/App.css';

import AppToolbar from './AppToolbar';

// Temp imports
import AppTest from './components/app-test';

export default class App extends React.Component {

  render() {
    return (
      <div className="App">
        <AppToolbar />
        <div>&nbsp;</div>
        <AppTest/>
      </div>
    );
  }
}
