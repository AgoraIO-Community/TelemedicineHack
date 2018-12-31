import React, { Component } from "react";
import NewWindow from 'react-new-window';
import * as firebase from 'firebase';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import Home from './Home';
import Appointment from './Appointment';
import {channel,ip,port} from "./static/property";

const styles = {
    fontFamily: 'sans-serif',
    textAlign: 'center',
};

class Account extends Component {

    constructor(props) {
        super(props);
        this.state = {
            join: true,
            mobile: ""
        };
    }

    onJoin = () => {
        this.setState({
            join: true
        });

    }

    // componentDidMount() {
    //     console.log("props in component did mount  = "+this.props.data)
    //     firebase.database().ref().child(acc).once('value', (data) => {
    //         let mobileNo = data.val();
    //         console.log("mobilenNo ==== " + mobileNo);
    //         if (mobileNo != "") {
    //             this.setState({
    //                 join: true,
    //                 mobile: mobileNo
    //             });
    //         } else {
    //             this.setState({
    //                 join: false
    //             });
    //         }
    //     });
    // }



    render() {
        let newWind
        if (this.state.join == true) {
            let finalUrl = "http://" + ip + ":" + port + "/meeting.html?account=" + channel;
            newWind = <NewWindow url={finalUrl} />
        }
        const displayPosts = (
            <div style={styles}>
                <Tabs defaultIndex={1}>
                    <TabList>
                        <Tab>Home</Tab>
                        <Tab>Appointments</Tab>
                    </TabList>
                    <TabPanel><Home /></TabPanel>
                    <TabPanel><Appointment data={this.props.data} /></TabPanel>
                </Tabs>
            </div>
        );
        return (
            <div style={styles}>
                <h1>Welcome user {this.props.data.userName} </h1>
                {newWind}
                {displayPosts}
            </div>
        );
    }
}

export default Account;