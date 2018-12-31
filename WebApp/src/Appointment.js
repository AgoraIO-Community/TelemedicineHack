import React, { Component } from "react";
import * as firebase from 'firebase';
import { Table } from 'antd';



const columns = [{
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
}, {
    title: 'time',
    dataIndex: 'time',
    key: 'time',
}, {
    title: 'slot',
    dataIndex: 'slot',
    key: 'slot',
}];

class Appointment extends Component {

    constructor(props) {
        super(props);
        console.log("JSON.stringify(props)" + JSON.stringify(props))
        console.log("constructor data = " + props.data.userName)
        this.state = {
            patientArr: [],
            mobile: ""
        };
    }

    componentDidMount(props) {
        let dataArr = [];
        let i = 0;
        let doctorName = this.props.data.userName;
        console.log("doctorName = == " + doctorName);
        let appoint = "/Appointments/" + doctorName + "/patients";
        firebase.database().ref().child(appoint).once('value', (data) => {
            data.forEach(child => {
                dataArr.push({
                    key: i,
                    name: child.key,
                    time: child.val().time,
                    slot: child.val().slot
                });
                i++;
            })
        }).then(() => {
            this.setState({
                patientArr: dataArr
            }, () => {
                console.log("dataArray ===" + this.state.patientArr)
            });
        });
    }

    render() {
        return (
            <div>
                your Appointment Table
                <Table columns={columns} dataSource={this.state.patientArr} />
            </div>
        );
    }
}

export default Appointment;