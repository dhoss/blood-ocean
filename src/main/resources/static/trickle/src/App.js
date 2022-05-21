import './App.css';
import 'video-react/dist/video-react.css';
import React from 'react';
import Async from "react-async";
import { Player } from 'video-react';

const loadVideo = () =>
  fetch("/api/videos/fixed-trimmed-end-sample.mp4")
    .then(res => (res.ok ? res : Promise.reject(res)))
    .then(res => res.json());
    
const App = () => (
    <Async promiseFn={loadVideo}>
      {({ data, error, isLoading }) => {
        if (isLoading) return "Loading...";
        if (error) return `Something went wrong: ${error.message}`;
        if (data)
          return (
              <Player
                playsInline
                source src={`https://trickle-media.sfo3.cdn.digitaloceanspaces.com/${data.path}?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=HFR3ZTLLWMR43DB54UYV%2F20220521%2Fsfo3%2Fs3%2Faws4_request&X-Amz-Date=20220521T221447Z&X-Amz-Expires=3600&X-Amz-SignedHeaders=host&X-Amz-Signature=8784b9b3a6260b4341b599212ab9e047c1f86ff361205d393eceacaaff17ae89`} 
              />
          );
        return null;
      }}
    </Async>
  );

export default App;