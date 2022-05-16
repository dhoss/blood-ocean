import logo from './logo.svg';
import './App.css';
import 'video-react/dist/video-react.css';
import React, { Component } from 'react';
import { Player } from 'video-react';

class App extends Component {
  render() {
      return (
        <Player>
          <source src="https://trickle-media.sfo3.cdn.digitaloceanspaces.com/fixed-trimmed-end-sample.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=HFR3ZTLLWMR43DB54UYV%2F20220515%2Fsfo3%2Fs3%2Faws4_request&X-Amz-Date=20220515T225731Z&X-Amz-Expires=86400&X-Amz-SignedHeaders=host&X-Amz-Signature=ec2929d564bc92332253ebb16bf341cc52b81ed65a627790ece4d3b08eac365b" />
        </Player>
      );
  }
}

export default App;