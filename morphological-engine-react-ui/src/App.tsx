import * as React from 'react';
import './App.css';
import AppToolbar from './AppToolbar';

import 'primereact/resources/themes/saga-green/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';
import 'primeflex/primeflex.css';

export default class App extends React.Component {

  render() {
    return (
      <div className="App">
        <AppToolbar />
      </div>
    );
  }
}
