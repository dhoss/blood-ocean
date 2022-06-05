import './App.css';
import React, { useState } from 'react';
import { useAsync } from "react-async";
import ReactPlayer from 'react-player'
import { Modal } from 'react-responsive-modal';

import 'react-responsive-modal/styles.css';
import './video-modal.css';
    
const loadVideo = () =>
  fetch("/api/videos")
    .then(res => (res.ok ? res : Promise.reject(res)))
    .then(res => res.json());

const App = () => {

  const [open, setOpen] = useState(false);
  const [video, setVideo] = useState({});

  const onOpenModal = (video) => {
    setVideo(video);
    setOpen(true);
  }

  const onCloseModal = () => setOpen(false);
  const { data, error, isPending } = useAsync({ promiseFn: loadVideo })

  if (isPending) return "Loading..."
  if (error) return `Something went wrong: ${error.message}`

  if (data)
    return (
      <div className={"bg-cover bg-blood-ocean"}>
        <div className="grid place-items-center h-screen">
          <section class="overflow-hidden text-gray-700 ">
            <div class="container px-5 py-2 mx-auto lg:pt-12 lg:px-32">
              <div class="flex flex-wrap -m-1 md:-m-2">
                {data.map(video =>
                <div class="flex flex-wrap w-1/3">
                  <div class="w-full p-1 md:p-2">
                    <img
                      className='video-modal-button'
                      onClick={() => onOpenModal(video)}
                      src={`${video.thumbnailUrl}`}
                      alt=""
                    />
                  </div>
                </div>
                )}
              </div>
            </div>
          </section>
            <Modal 
              open={open} 
              onClose={onCloseModal} 
              center
              classNames={{
                modal: 'video-modal',
              }}>
                <ReactPlayer 
                    url={`${video.url}`}
                    controls={true}
                    className="react-player"
                    playing
                    width="100%"
                    height="100%"
                />
            </Modal>
        </div>
      </div>
    );
  return null;
};

export default App;