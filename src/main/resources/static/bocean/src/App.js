import './App.css';
import React from 'react';
import Async from "react-async";
import ReactPlayer from 'react-player'

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
            <div className='grid place-items-center h-screen'>
                <ReactPlayer 
                    url={`https://trickle-media.sfo3.cdn.digitaloceanspaces.com/${data.path}?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=HFR3ZTLLWMR43DB54UYV%2F20220525%2Fsfo3%2Fs3%2Faws4_request&X-Amz-Date=20220525T032414Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=6311917430c8786a0cd5c53a3fc6f91415fbafb5c13477062cd7737fdfc4eb33`}
                    controls={true}
                    className="react-player"
                    playing
                    width="50%"
                    height="50%"
                />
            </div>
          );
        return null;
      }}
    </Async>
  );

export default App;