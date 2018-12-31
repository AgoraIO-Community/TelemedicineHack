import React, { Component } from "react";
import Button from 'antd/lib/button';
import 'antd/lib/button/style/css';
import Login from './Login';
import Register from './Register';

class SuccessfulRegister extends Component{

    constructor(props){
        super(props);
        this.state = {  
            loginPage:false,
            registerPage:false
        };
    }

    render(){
        if(this.state.loginPage === true){
            return <Login/>
        }
        if(this.state.loginPage === true){
            return <Register/>
        }
        return (
            <div className="register">
                <h1>Congratulations you have successflly registered</h1>
                <Button type="primary" onClick={() =>{this.setState({ loginPage:true});}}>Login</Button>
                <Button type="primary" onClick={() =>{this.setState({ registerPage:true});}}>Register</Button>
            </div>
            
        );
    }
}

export default SuccessfulRegister;