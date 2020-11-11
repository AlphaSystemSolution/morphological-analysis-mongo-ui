import * as React from 'react';
import './App.css';

import 'primereact/resources/themes/saga-green/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';
import 'primeflex/primeflex.css';

export default class App extends React.Component {

  state = {
    theme: 'saga-green',
    inputStyle: 'outlined',
    ripple: true,
    darkTheme: false,
    sidebarActive: false,
    newsActive: false,
    configuratorActive: false,
    changelogActive: false,
    searchVal: null
  };

  render() {
    return (
      <div className="App">Hello</div>
    );
  }
}
