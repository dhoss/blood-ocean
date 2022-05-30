import './App.css';
import React, { useState } from 'react';
import { useAsync } from "react-async";
import ReactDOM from 'react-dom';
import ReactPlayer from 'react-player'
import { Modal } from 'react-responsive-modal';

import 'react-responsive-modal/styles.css';
import './video-modal.css';
    
const loadVideo = () =>
  fetch("/api/videos/fixed-trimmed-end-sample.mp4")
    .then(res => (res.ok ? res : Promise.reject(res)))
    .then(res => res.json());

const App = () => {

  const [open, setOpen] = useState(false);

  const onOpenModal = () => setOpen(true);
  const onCloseModal = () => setOpen(false);
  const { data, error, isPending } = useAsync({ promiseFn: loadVideo })

  if (isPending) return "Loading..."
  if (error) return `Something went wrong: ${error.message}`

  if (data)
    return (
      <div className='grid place-items-center h-screen'>
          <button onClick={onOpenModal}>Open modal</button>
          <Modal 
            open={open} 
            onClose={onCloseModal} 
            center
            classNames={{
              modal: 'video-modal',
            }}>
              <ReactPlayer 
                  url={`https://trickle-media.sfo3.cdn.digitaloceanspaces.com/${data.path}?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=HFR3ZTLLWMR43DB54UYV%2F20220525%2Fsfo3%2Fs3%2Faws4_request&X-Amz-Date=20220525T032414Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=6311917430c8786a0cd5c53a3fc6f91415fbafb5c13477062cd7737fdfc4eb33`}
                  controls={true}
                  className="react-player"
                  playing
                  width="100%"
                  height="100%"
              />
          </Modal>
      </div>
    );
  return null;
};

export default App;