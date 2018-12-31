import React, { Component } from "react";
import Input from 'antd/lib/input';
import 'antd/lib/input/style/css';
import Button from 'antd/lib/button';
import 'antd/lib/button/style/css';
import * as firebase from 'firebase';
import Account from './Account';


const dataName = "doctors";


class Login extends Component {

  constructor(props) {
    super(props);
    this.state = {
      email: "",
      password: "",
      status: false,
      message: "",
      category: "",
      languagesKnown: "",
      mobile: "",
      userName: "",
      fromTime: "",
      toTime: ""
    };
  }


  onChange = event => {
    this.setState({
      [event.target.name]: event.target.value
    });
  }

  onSubmit = event => {
    event.preventDefault();
    let uniq = this.state.email.split("@")[0];
    let path = dataName + "/" + uniq;

    firebase.auth().signInWithEmailAndPassword(this.state.email, this.state.password).then(() => {
      this.setState({
        message: "user logged in"
      }, () => {
        firebase.database().ref().child(path).once('value', (data) => {
          console.log("login username ===" + data.val().userName)
          this.setState({
            status: true,
            userName: data.val().userName,
            category: data.val().category,
            languagesKnown: data.val().languagesKnown,
            mobile: data.val().mobile,
            fromTime: data.val().fromTime,
            toTime: data.val().toTime
          });
        });
      });
    }).catch(() => {
      this.setState({
        status: false
      });
    });
  }

  render() {
    if (this.state.status) {
      return <Account data={this.state} />
    }
    return (
      <div className="login">
        <h1>Login  Here</h1>
        <Input placeholder="email" type="email" style={{ width: 200 }}
          value={this.state.email}
          name="email"
          onChange={this.onChange}
        />
        <br /><br />
        <Input placeholder="password" style={{ width: 200 }}
          value={this.state.password}
          name="password"
          type="password"
          onChange={this.onChange}
        />
        <br /><br />
        <Button type="primary" onClick={this.onSubmit}>Login</Button>
        <p>{this.state.message}</p>
      </div>
    );
  }
}

export default Login;