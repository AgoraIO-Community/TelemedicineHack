import React, { Component } from "react";
import Input from 'antd/lib/input';
import 'antd/lib/input/style/css';
import Button from 'antd/lib/button';
import 'antd/lib/button/style/css';
import * as firebase from 'firebase';
import SuccessfulRegister from './SuccessfulRegister';
import { TimePicker } from 'antd';
import moment from 'moment';

const doctorCategories = 'DoctorCategories/categories';

const dataName = "doctors";


class Register extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: "",
      password: "",
      email: "",
      password: "",
      fromTime: "",
      toTime: "",
      status: false,
      message: "",
      mobile: "",
      languagesKnown: "",
      categoryValue: "",
      category: []
    };
  }

  componentDidMount() {
    firebase.database().ref().child(doctorCategories).once('value', (data) => {
      let categoryList = data.val().split(',');
      this.setState({
        category: categoryList,
        categoryValue: categoryList[0]
      });
    });
  }

  handleChangeDropDown = event => {
    this.setState({
      categoryValue: event.target.value
    });
  }

  onChange = event => {
    this.setState({
      [event.target.name]: event.target.value
    });
  }

 onChangeFromTime = date =>{
    // console.log(time, timeString);
    console.log("timeString === "+date.format('HH:mm'));
    this.setState({
      fromTime: date.format('HH:mm')
    });
  }

  onChangeToTime = date =>{
    // console.log(time, timeString);
    this.setState({
      toTime: date.format('HH:mm')
    });
  }

  handleChangeDropDown = event => {
    this.setState({
      categoryValue: event.target.value
    });
  }

  insertIntoDb() {
    let uniq = this.state.email.split("@")[0];
    let path = dataName + "/" + uniq;
    firebase.database().ref(path).set({
      userName: this.state.username,
      email: this.state.email,
      category: this.state.categoryValue,
      fromTime: this.state.fromTime,
      toTime: this.state.toTime,
      languagesKnown: this.state.languagesKnown,
      mobile: this.state.mobile
    }).then(() => {
      console.log('INSERTED in username!');
    }).catch((error) => {
      console.log(error)
    });
    let path1 = dataName + "/" + this.state.categoryValue + "/" + this.state.username;
    console.log(path1);
    firebase.database().ref(path1).set({
      userName: this.state.username,
      email: this.state.email,
      fromTime: this.state.fromTime,
      toTime: this.state.toTime,
      languagesKnown: this.state.languagesKnown,
      mobile: this.state.mobile
    }).then(() => {
      console.log('INSERTED in category !');
    }).catch((error) => {
      console.log(error)
    });

  }

  onSubmit = event => {
    event.preventDefault();
    firebase.auth().signInWithEmailAndPassword(this.state.email, this.state.password).then(() => {
      this.setState({
        message: "user already exists"
      });
    }).catch(() => {
      firebase.auth().createUserWithEmailAndPassword(this.state.email, this.state.password).then(() => {
        this.setState({
          message: "user successfully created",
          status: true
        });
        this.insertIntoDb();
      }).catch(() => {
        this.setState({
          message: "user creation failed"
        });
      });
    });
  }

  render() {
    let categoryItems = this.state.category;
    let listOpts = [];
    let list = this.state.category;
    for (var i = 0; i < list.length; i++) {
      listOpts.push(<option value={list[i]}>{list[i]}</option>);
    }
    if (this.state.status === true) {
      return <SuccessfulRegister />
    }
    return (
      <div className="register">
        <h1>{this.state.message}</h1>
        <h1>Register Here</h1>
        <Input placeholder="username" style={{ width: 200 }}
          value={this.state.username}
          name="username"
          onChange={this.onChange}
        />
        <br /><br />
        <Input placeholder="email" type="email" style={{ width: 200 }}
          value={this.state.email}
          name="email"
          onChange={this.onChange}
        />
        <br /><br />
        <Input placeholder="password" style={{ width: 200 }}
          value={this.state.password}
          type="password"
          name="password"
          onChange={this.onChange}
        />
        <br /><br />
        <Input placeholder="Languages Known" style={{ width: 200 }}
          value={this.state.languagesKnown}
          name="languagesKnown"
          onChange={this.onChange}
        />
        <br /><br />
        <Input placeholder="Mobile Number" style={{ width: 200 }}
          name="mobile"
          value={this.state.mobile}
          onChange={this.onChange}
        />
        <br /><br />
        <TimePicker name="fromTime" style={{ width: 200 }} onChange={this.onChangeFromTime} placeholder="From Time" defaultOpenValue={moment('00:00:00', 'HH:mm:ss')} />
        <br></br>
        <br></br>
        <TimePicker name="toTime" style={{ width: 200 }} onChange={this.onChangeToTime} placeholder="To Time" defaultOpenValue={moment('00:00:00', 'HH:mm:ss')} />
        <br></br>
        <br></br>
        <select onChange={this.handleChangeDropDown} style={{ width: 200 }}>
          {listOpts}
        </select>
        <br /><br />
        <Button type="primary" onClick={this.onSubmit}>Register</Button>
      </div>
    );
  }
}

export default Register;