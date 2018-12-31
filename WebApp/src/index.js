import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import * as serviceWorker from './serviceWorker';
// import Shivam from './Shivam';
import 'firebase/auth';

import * as firebase from 'firebase';

var config ={
    apiKey: "AIzaSyC6aPwkTKVUFwzqsr38I2SlBZBsBZWB4qQ",
    authDomain:"consultdoctors-f054f.firebaseapp.com",
    databaseURL: "https://consultdoctors-f054f.firebaseio.com",
    storageBucket:"consultdoctors-f054f.appspot.com",
};

firebase.initializeApp(config);

ReactDOM.render(<App />, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: http://bit.ly/CRA-PWA
serviceWorker.unregister();
