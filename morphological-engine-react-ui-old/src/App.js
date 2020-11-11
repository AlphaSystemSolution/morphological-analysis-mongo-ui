import React, { Component } from 'react';
import './App.css';
import AppToolbar from './AppToolbar';
import 'primereact/resources/themes/saga-green/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

export class App extends Component {

  constructor(props) {
    super(props);

    this.state = {
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
  }

  render() {
    return (
      <div className="App">
        <AppToolbar />
      </div>
    );
  }
}

export default App;
