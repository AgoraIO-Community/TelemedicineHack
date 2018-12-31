import React, { Component } from 'react';
import Login from './Login';
import Register from './Register';
import './App.css';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import Button from 'antd/lib/button';
import 'antd/lib/button/style/css';

class App extends Component {

  constructor(props) {
    super(props);
    this.state = {
      loginPage: false,
      registerPage: false

    };
  }
  render() {
    if (this.state.loginPage) {
      return <Login />;
    }
    if (this.state.registerPage) {
      return <Register />;
    }

    return (
      <div className="App">
        <Button type="primary" onClick={() => { this.setState({ loginPage: true, registerPage: false }); }}>Login</Button>
        <Button type="primary" onClick={() => { this.setState({ loginPage: false, registerPage: true }); }}>Register</Button>
      </div>
    );
  }
}

export default App;
