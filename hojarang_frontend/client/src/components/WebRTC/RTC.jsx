import { OpenVidu } from 'openvidu-browser';

import React, { useCallback, useEffect, useRef, useState } from 'react';
import UserVideoComponent from './UserVideoComponent';
import { createSession, createToken } from './Util';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

export default function RTC() {
  const nickname = useSelector((state) => state.userinfo.nickname)
  const [mySessionId, setMySessionId] = useState('SessionA')
  const [myUserName, setMyUserName] = useState(nickname)
  const navigate = useNavigate()

  // const [numOfVideos, setNumOfVideos] = useState(0);
  const handleChangeSessionId = useCallback((e) => {
    setMySessionId(e.target.value);
  }, []);

  const handleChangeUserName = useCallback((e) => {
    setMyUserName(e.target.value);
  }, []);


  const joinRoom = () =>{
    navigate(`/webrtc/${mySessionId}`)
  }

  return (
    <div className="container">
      <div id="join">
        <div id="join-dialog" className="jumbotron vertical-center">
          <h1> Join a video session </h1>
          <form className="form-group" onSubmit={joinRoom}>
            <p>
              <label>Participant: </label>
              <input
                className="form-control"
                type="text"
                id="userName"
                value={myUserName}
                onChange={handleChangeUserName}
                required
              />
            </p>
            <p>
              <label> Session: </label>
              <input
                className="form-control"
                type="text"
                id="sessionId"
                value={mySessionId}
                onChange={handleChangeSessionId}
                required
              />
            </p>
            <p className="text-center">
              <input className="btn btn-lg btn-success" name="commit" type="submit" value="JOIN" />
            </p>
          </form>
      </div>
    </div>
    </div>
  );
}